mapboxgl.accessToken = 'pk.eyJ1IjoiaGFpemFnZyIsImEiOiJjbWFqYXZybjgwdnE1MmtvcGd1NTZ2MXV5In0.P0SyaKDXdSqh_jg2CGpVcA';
let map;
let allMarkers = [];
let searchMarker;
let searchCircle;
const searchRadius = 1000;
let destinationModal;
let nearbyModal;
$(document).ready(function () {
    $('#datetime').datetimepicker({
        format: 'MM/DD/YYYY h:mm A', // Định dạng chính xác
        useCurrent: false
    });
});
function initMap() {
    map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/satellite-v9',
        center: [106.6018, 8.6955],
        zoom: 13
    });

    map.on('load', () => {
        destinationModal = new bootstrap.Modal(document.getElementById('destinationModal'));
        nearbyModal = new bootstrap.Modal(document.getElementById('nearbyModal'));
        loadMarkers();

        map.on('click', (e) => {
            const lngLat = e.lngLat;
            placeSearchMarker([lngLat.lng, lngLat.lat]);
        });
    });
}

function placeSearchMarker(lngLat) {
    if (searchMarker) searchMarker.remove();
    if (map.getSource('search-circle')) {
        map.removeLayer('search-circle-layer');
        map.removeSource('search-circle');
    }

    searchMarker = new mapboxgl.Marker({
        draggable: true,
        color: '#FF0000'
    })
        .setLngLat(lngLat)
        .addTo(map);

    drawSearchCircle(lngLat);

    searchMarker.on('dragend', () => {
        const newPos = searchMarker.getLngLat();
        drawSearchCircle([newPos.lng, newPos.lat]);
        fetchNearbyServices(newPos.lat, newPos.lng, searchRadius);
    });

    fetchNearbyServices(lngLat[1], lngLat[0], searchRadius);
}

function drawSearchCircle([lng, lat]) {
    const circleData = createGeoJSONCircle([lng, lat], searchRadius);

    map.addSource('search-circle', {
        type: 'geojson',
        data: circleData
    });

    map.addLayer({
        id: 'search-circle-layer',
        type: 'fill',
        source: 'search-circle',
        paint: {
            'fill-color': '#1a73e8',
            'fill-opacity': 0.2
        }
    });
}

function createGeoJSONCircle(center, radiusInMeters, points = 64) {
    const coords = { latitude: center[1], longitude: center[0] };
    const km = radiusInMeters / 1000;
    const ret = [];
    const distanceX = km / (111.320 * Math.cos(coords.latitude * Math.PI / 180));
    const distanceY = km / 110.574;

    for (let i = 0; i < points; i++) {
        const theta = (i / points) * (2 * Math.PI);
        const x = distanceX * Math.cos(theta);
        const y = distanceY * Math.sin(theta);
        ret.push([coords.longitude + x, coords.latitude + y]);
    }
    ret.push(ret[0]);
    return {
        type: 'Feature',
        geometry: {
            type: 'Polygon',
            coordinates: [ret]
        }
    };
}

function loadMarkers() {
    allMarkers.forEach(m => m.remove());
    allMarkers = [];

    fetch("/api/tourism/all")
        .then(res => res.json())
        .then(data => {
            data.forEach(service => {
                if (!service.latitude || !service.longitude || !service.name) return;

                const marker = new mapboxgl.Marker()
                    .setLngLat([service.longitude, service.latitude])
                    .addTo(map);

                // Thêm sự kiện nhấp chuột cho marker
                marker.getElement().addEventListener('click', (e) => {
                    // Ngăn sự kiện lan tỏa đến bản đồ
                    e.stopPropagation();

                    // Hiển thị modal chi tiết
                    document.getElementById("modal-title").innerText = service.name;
                    document.getElementById("modal-description").innerText = service.description || 'No description available';
                    document.getElementById("modal-map-link").href = `https://www.google.com/maps?q=${service.latitude},${service.longitude}`;
                    document.getElementById("view-details").href = `/tourism-details/${service.id}`;

                    const imageContainer = document.getElementById("modal-images");
                    imageContainer.innerHTML = 'Loading images...';

                    fetch(`/api/tourism/images/${service.id}`)
                        .then(res => res.json())
                        .then(images => {
                            imageContainer.innerHTML = '';
                            if (images.length > 0) {
                                images.forEach(img => {
                                    const imgElement = document.createElement('img');
                                    imgElement.src = img.imagePath;
                                    imgElement.className = 'preview-image';
                                    // ✅ Thêm sự kiện phóng to ảnh ở đây
                                    imgElement.addEventListener('click', () => {
                                        const overlay = document.createElement('div');
                                        overlay.className = 'image-lightbox-overlay';
                                        overlay.innerHTML = `<img src="${img.imagePath}" alt="Preview">`;

                                        // Click vào overlay để đóng
                                        overlay.addEventListener('click', () => overlay.remove());

                                        document.body.appendChild(overlay);
                                    });
                                    imageContainer.appendChild(imgElement);
                                });
                            } else {
                                imageContainer.innerText = 'No images available';
                            }
                        }).catch(() => {
                            imageContainer.innerText = 'Error loading images';
                        });

                    destinationModal.show();
                });

                allMarkers.push(marker);
            });
        });
};

function fetchNearbyServices(lat, lng, radius) {
    const container = document.getElementById("nearby-services");
    if (!container) return;
    container.innerHTML = 'Loading nearby services...';

    fetch(`/api/tourism/nearby?lat=${lat}&lng=${lng}&radius=${radius}`)
        .then(res => res.json())
        .then(services => {
            container.innerHTML = '';
            if (services && services.length > 0) {
                services.forEach(service => {
                    const serviceDiv = document.createElement('div');
                    serviceDiv.className = 'service-item';
                    serviceDiv.innerHTML = `
                            <h6>${service.name || 'Unknown'}</h6>
                            <p>${service.description || 'No description'}</p>
                            <a href="https://www.google.com/maps?q=${service.latitude},${service.longitude}" target="_blank" class="btn btn-sm">View on Google Maps</a>
                            <a href="/tourism-details/${service.id}" class="btn btn-sm">View Details</a>
                        `;
                    container.appendChild(serviceDiv);
                });
            } else {
                container.innerText = 'No services found within 1km radius.';
            }
            nearbyModal.show();
        })
        .catch(() => {
            container.innerHTML = 'Error loading nearby services';
            nearbyModal.show();
        });
};


window.addEventListener('load', () => {
    initMap();
});
