
// Hàm load danh sách tỉnh
async function loadProvinces() {
    try {
        let response = await fetch('/api/provinces');
        let provinces = await response.json();
        let select = document.getElementById("province");
        select.innerHTML = "<option value=''>Chọn tỉnh</option>";
        provinces.forEach(province => {
            let option = document.createElement("option");
            option.value = province.code; // sử dụng mã tỉnh
            option.text = province.name;
            select.appendChild(option);
        });
    } catch (error) {
        console.error("Lỗi loadProvinces:", error);
    }
}

// Hàm load danh sách huyện theo mã tỉnh
async function loadDistricts(provinceCode) {
    let districtSelect = document.getElementById("district");
    districtSelect.innerHTML = '<option value="">--Chọn quận/huyện--</option>';
    // Reset danh sách xã
    document.getElementById("ward").innerHTML = '<option value="">--Chọn phường/xã--</option>';
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
    console.log(captalizeFirstLetter);
    console.log(selectedProvince);
    if (inputAddress.includes(selectedProvince) || inputAddress.includes(captalizeFirstLetter) || inputAddress.includes(selectedProvinceWithSpace)) {
        errorSpan.style.display = "none";
        return true; // Cho phép submit
    }

    errorSpan.style.display = "block";
    return false; // Không cho phép submit
}
// Khi trang tải, load danh sách tỉnh
window.onload = loadProvinces;