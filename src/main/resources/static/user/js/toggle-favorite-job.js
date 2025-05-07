$(document).ready(function () {
  let csrfToken = $('meta[name="_csrf"]').attr("content");
  let csrfHeader = $('meta[name="_csrf_header"]').attr("content");
  $.ajaxSetup({
    beforeSend: function (xhr) {
      xhr.setRequestHeader(csrfHeader, csrfToken);
    },
  });

  $(document).on("click", "#handle-add-delete-favorite-job", function () {
    if ($("#not-signed-in").length > 0) {
      toastr.warning(
        "Bạn cần đăng nhập để thực hiện thao tác này",
        "Lỗi thao tác"
      );
      return;
    }
    let jobId = $(this).data("job-id");
    let isAdding = $(this).find("i").hasClass("far");
    let url = isAdding
      ? `/seeker/api/add-favorite-job/${jobId}`
      : `/seeker/api/delete-favorite-job/${jobId}`;
    let successMessage = isAdding
      ? "Thêm công việc yêu thích thành công"
      : "Xóa công việc yêu thích thành công";

    $.ajax({
      url: url,
      type: "POST",
      contentType: "application/json",
      success: function (response) {
        toastr.success(successMessage, "Thành công");
        let icon = $("#handle-add-delete-favorite-job i");
        icon.toggleClass("far fa");
      },
      error: function (xhr, status, error) {
        alert("Có lỗi xảy ra, check code đi mậy: " + error);
      },
    });
  });
});
