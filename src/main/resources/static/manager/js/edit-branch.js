
let map;
let marker;

function initMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 21.0285, lng: 105.8542 }, // Hà Nội mặc định
        zoom: 13,
    });

    marker = new google.maps.Marker({
        position: { lat: 21.0285, lng: 105.8542 },
        map,
        draggable: true, // Cho phép kéo marker
    });

    const input = document.getElementById("autocomplete");
    const autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.bindTo("bounds", map);

    autocomplete.addListener("place_changed", () => {
        const place = autocomplete.getPlace();
        if (!place.geometry || !place.geometry.location) return;
        map.setCenter(place.geometry.location);
        marker.setPosition(place.geometry.location);
        updateAddressByCoords(place.geometry.location);
    });

    marker.addListener("dragend", () => {
        const pos = marker.getPosition();
        updateAddressByCoords(pos);
    });
}

function updateAddressByCoords(location) {
    let geocoder = new google.maps.Geocoder();
    geocoder.geocode({ location: location }, function (results, status) {
        if (status === "OK" && results[0]) {
            let address = results[0].formatted_address;

            // Loại bỏ Plus Code (chuỗi có ký tự "+" ở đầu)
            let addressParts = address.split(",");
            if (addressParts[0].match(/\+/)) {
                addressParts.shift(); // Xóa phần tử đầu tiên nếu chứa "+"
            }

            let cleanedAddress = addressParts.join(",").trim();
            document.getElementById("address").value = cleanedAddress;
        } else {
            console.log("Không thể lấy địa chỉ:", status);
        }
    });
}

function updateMapLocation() {
    let province = document.getElementById("province").options[document.getElementById("province").selectedIndex].text;
    let district = document.getElementById("district").options[document.getElementById("district").selectedIndex].text;
    let ward = document.getElementById("ward").options[document.getElementById("ward").selectedIndex].text;

    let fullAddress = "";
    if (ward && ward !== "--Chọn phường/xã--") fullAddress += ward + ", ";
    if (district && district !== "--Chọn quận/huyện--") fullAddress += district + ", ";
    if (province && province !== "Chọn tỉnh") fullAddress += province;

    if (fullAddress.trim() !== "") {
        let geocoder = new google.maps.Geocoder();
        geocoder.geocode({ address: fullAddress }, function (results, status) {
            if (status === "OK") {
                let location = results[0].geometry.location;
                map.setCenter(location);
                marker.setPosition(location);
            } else {
                console.log("Không thể tìm thấy vị trí:", status);
            }
        });
    }
}
function removeVietnameseTones(str) {
    return str.normalize("NFD") // Tách dấu khỏi chữ cái
        .replace(/[\u0300-\u036f]/g, '') // Xóa dấu
        .replace(/Đ/g, 'D').replace(/đ/g, 'd') // Xử lý riêng Đ, đ
        .replace(/\s+/g, ''); // Xóa khoảng trắng
}

function validateAddress() {
    let provinceElement = document.getElementById("province");
    let selectedProvince = provinceElement.options[provinceElement.selectedIndex].text.trim();

    // Loại bỏ "Tỉnh" hoặc "Thành phố"
    selectedProvince = selectedProvince.replace(/^(Tỉnh|Thành phố)\s+/i, "");
    let selectedProvinceNoTone = removeVietnameseTones(selectedProvince);
    let selectedProvinceWithSpace = selectedProvinceNoTone.replace(/([A-Z])/g, ' $1').trim();
    let captalizeFirstLetter = selectedProvinceNoTone.toLowerCase().charAt(0).toUpperCase() + selectedProvinceNoTone.toLowerCase().slice(1);
    let inputAddress = document.getElementById("address").value.trim();
    let errorSpan = document.getElementById("address-error");

    console.log(selectedProvinceWithSpace);
    console.log(selectedProvinceNoTone);
    console.log(captalizeFirstLetter);
    console.log(selectedProvince);
    if (inputAddress.includes(selectedProvince) || inputAddress.includes(captalizeFirstLetter) || inputAddress.includes(selectedProvinceWithSpace)) {
        errorSpan.style.display = "none";
        return true; // Cho phép submit
    }

    errorSpan.style.display = "block";
    return false; // Không cho phép submit
}

// Hàm load danh sách tỉnh
async function loadProvinces() {
    try {
        let response = await fetch('/api/provinces');
        let provinces = await response.json();
        let select = document.getElementById("province");
        let selectedProvince = select.getAttribute("data-selected");
        console.log(selectedProvince);
        select.innerHTML = "<option value=''>Chọn tỉnh</option>";
        provinces.forEach(province => {
            let option = document.createElement("option");
            option.value = province.code; // sử dụng mã tỉnh
            option.text = province.name;
            select.appendChild(option);
        });
        if (selectedProvince) {
            select.value = selectedProvince;
            loadDistricts(selectedProvince);
        }
    } catch (error) {
        console.error("Lỗi loadProvinces:", error);
    }
}

// Hàm load danh sách huyện theo mã tỉnh
async function loadDistricts(provinceCode) {
    let districtSelect = document.getElementById("district");
    let wardSelect = document.getElementById("ward");
    districtSelect.innerHTML = '<option value="">--Chọn quận/huyện--</option>';
    wardSelect.innerHTML = '<option value="">--Chọn phường/xã--</option>';
    let selectedDistrict = districtSelect.getAttribute("data-selected");
    if (provinceCode) {
        try {
            const response = await fetch(`/api/districts/${provinceCode}`);
            const districts = await response.json();
            districts.forEach(district => {
                let option = document.createElement("option");
                option.value = district.code;
                option.text = district.name;
                districtSelect.appendChild(option);
            });
            districtSelect.removeAttribute("data-selected");
            if (selectedDistrict) {
                districtSelect.value = selectedDistrict;
                loadWards(selectedDistrict);
            }
        } catch (error) {
            console.error("Lỗi loadDistricts:", error);
        }
    }
    updateAddress(); // cập nhật lại địa chỉ
}

// Hàm load danh sách xã theo mã huyện
async function loadWards(districtCode) {
    let wardSelect = document.getElementById("ward");
    wardSelect.innerHTML = '<option value="">--Chọn phường/xã--</option>';
    let selectedWard = wardSelect.getAttribute("data-selected");
    if (districtCode) {
        try {
            const response = await fetch(`/api/wards/${districtCode}`);
            const wards = await response.json();
            wards.forEach(ward => {
                let option = document.createElement("option");
                option.value = ward.code;
                option.text = ward.name;
                wardSelect.appendChild(option);
            });
            wardSelect.removeAttribute("data-selected");
            if (selectedWard) {
                wardSelect.value = selectedWard;
            }
        } catch (error) {
            console.error("Lỗi loadWards:", error);
        }
    }
    updateAddress(); // cập nhật lại địa chỉ
}

// Hàm cập nhật ô địa chỉ dựa trên các select đã chọn
function updateAddress() {
    const provinceSelect = document.getElementById("province");
    const districtSelect = document.getElementById("district");
    const wardSelect = document.getElementById("ward");

    let address = "";
    if (wardSelect.value) {
        address += wardSelect.options[wardSelect.selectedIndex].text + ", ";
    }
    if (districtSelect.value) {
        address += districtSelect.options[districtSelect.selectedIndex].text + ", ";
    }
    if (provinceSelect.value) {
        address += provinceSelect.options[provinceSelect.selectedIndex].text;
    }
    document.getElementById("address").value = address;
    document.getElementById("autocomplete").value = "";

    // Cập nhật vị trí bản đồ nếu cần (gọi hàm updateMapLocation nếu đã định nghĩa)
    updateMapLocation();
}

// Gắn sự kiện onchange cho các select (nếu chưa gắn trực tiếp trong thẻ HTML)
document.getElementById("province").addEventListener("change", function () {
    loadDistricts(this.value);
});
document.getElementById("district").addEventListener("change", function () {
    loadWards(this.value);
});
document.getElementById("ward").addEventListener("change", function () {
    updateAddress();
});

// Khi trang tải, load danh sách tỉnh
window.onload = loadProvinces;