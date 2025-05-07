document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".topic-checkbox").forEach((checkbox) => {
    checkbox.addEventListener("change", function () {
      let label = document.querySelector(`label[for="${checkbox.id}"]`);
      label.classList.toggle("btn-primary", checkbox.checked);
      label.classList.toggle("btn-outline-primary", !checkbox.checked);
      label.classList.toggle("text-white", checkbox.checked);
    });
  });
});
function setSatisfaction(value, button) {
  let satisfactionInput = document.getElementById("satisfactionInput");
  let buttons = document.querySelectorAll(".satisfaction-btn");

  // Check if the clicked button is already selected
  if (button.classList.contains("btn-primary")) {
    // If already selected, unselect it
    button.classList.remove("btn-primary");
    button.classList.add("btn-outline-primary");
    satisfactionInput.value = '';  // Clear the value if unselected
  } else {
    // If not selected, select it
    buttons.forEach((btn) => {
      btn.classList.add("btn-outline-primary");
      btn.classList.remove("btn-primary");
    });
    satisfactionInput.value = value;
    button.classList.add("btn-primary");
    button.classList.remove("btn-outline-primary");
  }
}

function resetForm() {
  // Lấy form theo ID
  let form = document.getElementById('feedbackForm');

  // Kiểm tra nếu form tồn tại thì reset
  if (form) {
    form.reset();
  }

  // Đặt lại giá trị mức độ hài lòng
  let satisfactionInput = document.getElementById('satisfactionInput');
  if (satisfactionInput) {
    satisfactionInput.value = ''; // Đặt lại giá trị rỗng
  }

  // Bỏ trạng thái active của tất cả nút satisfaction
  document.querySelectorAll('.satisfaction-btn').forEach((btn) => {
    btn.classList.remove('btn-primary', 'text-white');
    btn.classList.add('btn-outline-primary');
  });

  // Xóa chọn checkbox topic
  document.querySelectorAll('.topic-checkbox').forEach((checkbox) => {
    checkbox.checked = false;
    let label = document.querySelector(`label[for="${checkbox.id}"]`);
    if (label) {
      label.classList.remove('btn-primary', 'text-white');
      label.classList.add('btn-outline-primary');
    }
  });
}

function clearForm() {
  let form = document.getElementById("supportForm");

  form.reset();

  form.querySelectorAll("input[type='text'], input[type='email'], textarea, select").forEach(field => {
    field.value = "";
  });

  // Đặt lại ảnh minh họa
  document.getElementById("imageFile1").value = "";
  document.getElementById("imageFile2").value = "";

  // Ẩn ảnh xem trước (hoặc đổi sang ảnh mặc định)
  document.getElementById("imagePreview1").src = ""; // Hoặc "" để ẩn
  document.getElementById("imagePreview2").src = ""; // Hoặc "" để ẩn
}

