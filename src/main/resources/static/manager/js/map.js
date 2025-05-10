
mapboxgl.accessToken = 'pk.eyJ1IjoiZHV5aHZoZTE4MDA1MCIsImEiOiJjbWFndGtvN2owNHFoMnFvZjhiYXJhZGQ1In0.hUgne7zIXBviCIb5yzDEuw'; // Thay bằng token Mapbox thật của bạn
let map;
let allMarkers = [];
let latClicked = 0, lngClicked = 0;
let clickedOnMarker = false;
let sidebar = new bootstrap.Offcanvas(document.getElementById('sidebar'));

function initMap() {
    map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/satellite-streets-v12',
        center: [106.6018, 8.6955], // (lng, lat)
        zoom: 13
    });

    map.addControl(new mapboxgl.NavigationControl());

    map.on('load', loadMarkers);

    map.on('click', function (e) {
        if (clickedOnMarker) {
            clickedOnMarker = false;
            return; // Bỏ qua nếu vừa click vào marker
        }

        latClicked = e.lngLat.lat;
        lngClicked = e.lngLat.lng;
        document.getElementById("lat").value = latClicked;
        document.getElementById("lng").value = lngClicked;
        new bootstrap.Modal(document.getElementById('serviceModal')).show();
    });
};

function loadMarkers() {
    allMarkers.forEach(m => m.remove());
    allMarkers = [];

    fetch("/api/tourism/all")
        .then(res => res.json())
        .then(data => {
            data.forEach(service => {
                const el = document.createElement('div');
                el.className = 'mapbox-marker';

                const marker = new mapboxgl.Marker(el)
                    .setLngLat([service.longitude, service.latitude])
                    .addTo(map);
                // Tạo một div chứa văn bản và thêm vào bản đồ
                const textDiv = document.createElement('div');
                textDiv.className = 'marker-text'; // Class CSS cho văn bản
                textDiv.innerHTML = `<strong>${service.name}</strong>`;
                document.getElementById('map').appendChild(textDiv);
                // Cập nhật vị trí của văn bản khi marker di chuyển hoặc bản đồ zoom
                const updateTextPosition = () => {
                    const pos = map.project([service.longitude, service.latitude]);  // Chuyển đổi từ lng/lat sang pixel position
                    textDiv.style.left = `${pos.x + 10}px`; // Dịch chuyển chút ra ngoài
                    textDiv.style.top = `${pos.y - 30}px`; // Dịch chuyển chút lên trên marker
                };

                updateTextPosition(); // Cập nhật vị trí ngay lập tức

                map.on('move', updateTextPosition); // Cập nhật vị trí khi bản đồ di chuyển
                map.on('zoom', updateTextPosition); // Cập nhật vị trí khi zoom thay đổi

                marker.getElement().addEventListener('click', (event) => {
                    clickedOnMarker = true;
                    event.stopPropagation();

                    document.getElementById("sidebar-title").innerText = service.name;
                    document.getElementById("sidebar-description").innerText = service.description;
                    document.getElementById("sidebar-map-link").href = `https://www.google.com/maps?q=${service.latitude},${service.longitude}`;
                    document.getElementById("sidebar-view-detail").href = `/admin/tourism-manage/view/${service.id}`;

                    document.getElementById("deleteButton").onclick = function () {
                        deleteTourism(service.id);
                    };

                    const imageContainer = document.getElementById("sidebar-images");
                    imageContainer.innerHTML = 'Loading images...';

                    fetch(`/api/tourism/images/${service.id}`)
                        .then(res => res.json())
                        .then(images => {
                            imageContainer.innerHTML = '';

                            if (images.length > 0) {
                                const mainImg = document.createElement('img');
                                mainImg.src = images[0].imagePath;
                                mainImg.className = 'main-image';
                                imageContainer.appendChild(mainImg);

                                const gallery = document.createElement('div');
                                gallery.className = 'image-gallery';
                                images.forEach(img => {
                                    const imgElement = document.createElement('img');
                                    imgElement.src = img.imagePath;
                                    imgElement.className = 'preview-image';
                                    imgElement.addEventListener('click', () => {
                                        mainImg.classList.remove('show');
                                        mainImg.classList.add('fade-in');
                                        mainImg.src = img.imagePath;
                                        mainImg.onload = () => {
                                            mainImg.classList.remove('fade-in');
                                            mainImg.classList.add('show');
                                        };
                                    });
                                    gallery.appendChild(imgElement);
                                });

                                imageContainer.appendChild(document.createElement('hr'));
                                imageContainer.appendChild(gallery);
                            } else {
                                imageContainer.innerText = 'No images available';
                            }
                        })
                        .catch(err => {
                            console.error('Lỗi khi lấy ảnh:', err);
                            imageContainer.innerText = 'Error loading images';
                        });

                    sidebar.show();
                });

                allMarkers.push(marker);
            });
        });
};

function deleteTourism(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: 'This will permanently delete the tourism item.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `/api/tourism/delete/${id}`,
                type: 'GET',
                success: function () {
                    Swal.fire('Deleted!', 'The tourism item has been deleted.', 'success');
                    sidebar.hide();
                    loadMarkers();
                },
                error: function () {
                    Swal.fire('Error!', 'Failed to delete tourism.', 'error');
                }
            });
        }
    });
};

window.onload = () => {
    initMap();

    const imageLocation = document.getElementById('imageLocation');
    const imagePreview = document.getElementById('imagePreview');

    imageLocation.addEventListener('change', () => {
        const files = imageLocation.files;
        imagePreview.innerHTML = '';
        if (files.length > 4) {
            Swal.fire({
                icon: 'error',
                title: 'Maximum 4 images',
                text: 'Please select no more than 4 images.',
                confirmButtonText: 'OK',
            });
            imageLocation.value = '';
            return;
        }
        for (const file of files) {
            if (file.type.match('image.*')) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    img.className = 'preview-image';
                    imagePreview.appendChild(img);
                };
                reader.readAsDataURL(file);
            }
        }
    });
};
document.getElementById("locateBtn").addEventListener("click", () => {
    if (!navigator.geolocation) {
        Swal.fire({
            icon: 'error',
            title: 'Unsupported',
            text: 'Your browser does not support location services.',
        });
        return;
    }

    navigator.geolocation.getCurrentPosition(
        (position) => {
            const lng = position.coords.longitude;
            const lat = position.coords.latitude;

            map.flyTo({ center: [lng, lat], zoom: 15 });

            new mapboxgl.Marker({ color: "blue" })
                .setLngLat([lng, lat])
                .setPopup(new mapboxgl.Popup().setText("You're here"))
                .addTo(map);
        },
        (error) => {
            Swal.fire({
                icon: 'error',
                title: 'Error retrieving location. Please enable location services on your device.',
                text: error.message,
            });
        }
    );
});


