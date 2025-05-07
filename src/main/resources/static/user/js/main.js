// Skill tag click handling
function addSkillSearch() {
  $(".skill-tag").click(function () {
    let skillId = $(this).data("skill-id");
    window.location.href = "/jobs?" + "requiredSkills=" + skillId;
  });
}

(function ($) {
  "use strict";
  // Spinner
  let spinner = function () {
    setTimeout(function () {
      if ($("#spinner").length > 0) {
        $("#spinner").removeClass("show");
      }
    }, 1);
  };
  spinner();

  $("#imageFile1").change(function (e) {
    const imgURL = URL.createObjectURL(e.target.files[0]);
    $("#imagePreview1").attr("src", imgURL);
  });

  $("#imageFile2").change(function (e) {
    const imgURL = URL.createObjectURL(e.target.files[0]);
    $("#imagePreview2").attr("src", imgURL);
  });
  // Sticky Navbar
  $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
      $(".sticky-top").css("top", "0px");
    } else {
      $(".sticky-top").css("top", "-100px");
    }
  });

  // Header carousel
  if ($(".header-carousel").length > 0) {
    $(".header-carousel").owlCarousel({
      autoplay: true,
      smartSpeed: 1500,
      items: 1,
      dots: true,
      loop: true,
      nav: true,
      navText: [
        '<i class="bi bi-chevron-left"></i>',
        '<i class="bi bi-chevron-right"></i>',
      ],
    });
  }

  // For skill tags
  $(document).ready(function () {
    addSkillSearch();
    if (document.getElementById("skills")) {
      // Initialize select2 for skills
      $("#skills").select2({
        placeholder: "Chọn kỹ năng",
        allowClear: true,
        width: "100%",
      });
    }

    if (document.getElementById("levels")) {
      $("#levels").select2({
        placeholder: "Cấp bậc",
        allowClear: true,
        width: "100%",
      });
    }

    if (document.getElementById("working-model")) {
      $("#working-model").select2({
        placeholder: "Mô hình làm việc",
        allowClear: true,
        width: "100%",
      });
    }
  });

  // add active navbar
  // Get the current URL path
  let currentPath = window.location.pathname;

  // Iterate through each nav-link
  $(".nav-item.nav-link").each(function () {
    let href = $(this).attr("href");
    // Check if the current URL path contains the href
    if (currentPath === "/") {
      console.log("currentPath", currentPath);
      $(".nav-item.nav-link[href='/']").addClass("active");
      return;
    }
    if (currentPath.includes(href) && href !== "/") {
      $(this).addClass("active");
    }
  });

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

  let url = window.location.href;
  let queryString = url.split("?")[1];
  switch (queryString) {
    case "old-pwd-error":
      toastr.error("Old password is incorrect", "Error");
      break;
    case "pwd-not-match":
      toastr.error("New password does not match", "Error");
      break;
    case "pwd-change-success":
      toastr.success("Password changed successfully", "Success");
      break;
    case "edit-success":
      toastr.success("Information edited successfully", "Success");
      break;
    case "update-avatar-success":
      toastr.success("Avatar updated successfully", "Success");
      break;
    case "update-avatar-fail":
      toastr.error("Failed to update avatar", "Error");
      break;

  }
})(jQuery);
