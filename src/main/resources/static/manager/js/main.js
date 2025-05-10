(function ($) {
  "use strict";
  // Spinner
  var spinner = function () {
    setTimeout(function () {
      if ($("#spinner").length > 0) {
        $("#spinner").removeClass("show");
      }
    }, 1);
  };
  spinner();

  // Back to top button
  $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
      $(".back-to-top").fadeIn("slow");
    } else {
      $(".back-to-top").fadeOut("slow");
    }
  });
  $(".back-to-top").click(function () {
    $("html, body").animate({ scrollTop: 0 }, 1500, "easeInOutExpo");
    return false;
  });

  // Sidebar Toggler
  $(".sidebar-toggler").click(function () {
    $(".sidebar, .content").toggleClass("open");
    return false;
  });

  // Logo preview
  $("#imageFile").change(function (e) {
    const imgURL = URL.createObjectURL(e.target.files[0]);
    $("#imagePreview").attr("src", imgURL);
  });
  $("#imageFile1").change(function (e) {
    const imgURL = URL.createObjectURL(e.target.files[0]);
    $("#imagePreview1").attr("src", imgURL);
  });

  $("#imageFile2").change(function (e) {
    const imgURL = URL.createObjectURL(e.target.files[0]);
    $("#imagePreview2").attr("src", imgURL);
  });

  function onSubmit(token) {
    document.getElementById("demo-form").submit();
  }

  // add active navbar
  // Get the current URL path
  var currentPath = window.location.pathname;

  // Iterate through each nav-link
  $(".nav-item.nav-link").each(function () {
    var href = $(this).attr("href");

    // Check if the current URL path contains the href
    if (currentPath.includes(href)) {
      $(this).addClass("active");
    }
  });

  if (document.getElementById("skills")) {
    // For select 2 library
    $("#skills").select2({
      placeholder: "Chọn kỹ năng",
      allowClear: true,
      width: "100%", // Ensures the Select2 dropdown fits within Bootstrap's grid
    });
  }
  if (document.getElementById("levels")) {
    // For select 2 library
    $("#levels").select2({
      placeholder: "Chọn các vị trí cần tuyển dụng",
      allowClear: true,
      width: "100%", // Ensures the Select2 dropdown fits within Bootstrap's grid
    });
  }

  // Modal for delete button
  $(document).ready(function () {
    let deleteLink = null;

    // Handle the click event of the delete link
    $(".delete-link").on("click", function (event) {
      event.preventDefault(); // Prevent default action (navigation)

      deleteLink = $(this).attr("href"); // Store the href of the clicked link

      // Show the modal
      $("#confirmDeleteModal").modal("show");
    });

    // Handle the confirmation button click in the modal
    $("#confirmDeleteBtn").on("click", function () {
      if (deleteLink) {
        window.location.href = deleteLink; // Redirect to the stored link
      }
    });
  });

  // tinymce editor
  if (document.getElementById("tinymce-editor")) {
    tinymce.init({
      selector: 'textarea#tinymce-editor',
      plugins: [
        "advlist",
        "autolink",
        "lists",
        "link",
        "image",
        "charmap",
        "preview",
        "anchor",
        "searchreplace",
        "visualblocks",
        "code",
        "fullscreen",
        "insertdatetime",
        "media",
        "table",
        "help",
        "wordcount",
      ],
      toolbar: "undo redo | blocks | " +
        "bold italic backcolor | alignleft aligncenter " +
        "alignright alignjustify | bullist numlist outdent indent | " +
        "removeformat | help",
      content_style:
        "body { font-family:Helvetica,Arial,sans-serif; font-size:16px }",
    });
  }

  toastr.options = {
    closeButton: true,
    debug: false,
    newestOnTop: false,
    progressBar: true,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut",
  };

  // Display an info toast with no title
  let url = window.location.href;
  let queryString = url.split("?")[1];
  switch (queryString) {
    case "register-success":
      toastr.success("Account registration successful", "Success");
      break;
    case "login-error":
      toastr.error("Login with OAuth2 failed", "Error");
      break;
    case "invalid":
      toastr.error("Incorrect username or password, login failed", "Error");
      break;
    case "oauth2-error":
    case "oauth2-error#_=_":
      toastr.error("OAuth2 login failed", "Error");
      break;
    case "disabled":
      toastr.error("Account is temporarily locked, please contact Admin", "Error");
      break;
    case "logout":
      toastr.success("Logout successful", "Success");
      break;
    case "success-booking":
      toastr.success("Booking successful", "Success");
      break;
    case "login-success":
      toastr.success("Login successful", "Success");
      break;
    case "old-pwd-error":
      toastr.error("Old password is incorrect", "Error");
      break;
    case "pwd-not-match":
      toastr.error("New password does not match", "Error");
      break;
    case "pwd-change-success":
      toastr.success("Password changed successfully", "Success");
      break;
    case "update-success":
      toastr.success("Information updated successfully", "Success");
      break;
    case "update-fail":
      toastr.error("Failed to update information", "Error");
      break;
    case "add-account-success":
      toastr.success("Account added successfully", "Success");
      break;
    case "add-account-fail":
      toastr.error("Failed to add account", "Error");
      break;

    case "booking-fail":
      toastr.error("Booking package tour fail", "Error");
      break;
    case "deactivate-success":
      toastr.success("Account deactivation successful", "Success");
      break;
    case "subscribe_fail":
      toastr.error("Sign up guide fail", "Error");
      break;
    case "subscribe_success":
      toastr.success("Sign up guide successful", "Success");
      break;
    case "activate-success":
      toastr.success("Account activation successful", "Success");
      break;
    case "add-success":
      toastr.success("Added successfully", "Success");
      break;
    case "delete-user-success":
      toastr.success("Account deleted successfully", "Success");
      break;
    case "invalid-user":
      toastr.error("Account or Gmail does not exist", "Error");
      break;
    case "skillexist":
      toastr.error("Skill already exists", "Error");
      break;
    case "skill-add-success":
      toastr.success("Skill added successfully", "Success");
      break;
    case "skill-edit-success":
      toastr.success("Skill edited successfully", "Success");
      break;
    case "skill-delete-success":
      toastr.success("Skill deleted successfully", "Success");
      break;
    case "verification-success":
      toastr.success("Account verification successful", "Success");
      break;
    case "email_existed":
      toastr.error("Email is existed", "Error");
      break;
    case "confirm_booking":
      toastr.success("Booking confirmed", "Success");
      break;


  }
})(jQuery);
