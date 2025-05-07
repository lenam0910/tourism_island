$(document).ready(function () {
    // Lấy companyId từ thẻ div
    const companyIdElement = document.getElementById("CompanyId");
    if (!companyIdElement) {
        console.error("Không tìm thấy phần tử với id 'CompanyId'");
        return;
    }
    const companyId = companyIdElement.textContent.trim(); // Sử dụng textContent thay vì value
    console.log("Company ID:", companyId);

    // Gọi API để lấy dữ liệu đánh giá
    $.get(`/api/companies/${companyId}/rating`, function (response) {
        const ratingsData = response.ratingData; // Dữ liệu tổng quan từ DTO

        // Cập nhật UI với dữ liệu từ API
        updateRatingsUI(ratingsData);
    }).fail(function () {
        console.error("Error fetching ratings data");
        // Hiển thị dữ liệu mặc định nếu API thất bại
        updateRatingsUI({
            average: 0.0,
            totalReviews: 0,
            stars: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 }
        });
    });

    function updateRatingsUI(data) {
        document.getElementById("average-rating").textContent = data.average.toFixed(1);
        document.getElementById("total-reviews").textContent = `${data.totalReviews} đánh giá`;

        // Hiển thị ngôi sao
        const starContainer = document.getElementById("star-container");
        starContainer.innerHTML = "";
        let fullStars = Math.floor(data.average);
        let halfStar = data.average % 1 >= 0.5;

        for (let i = 1; i <= 5; i++) {
            let starClass = "far fa-star";
            if (i <= fullStars) starClass = "fas fa-star checked";
            else if (i === fullStars + 1 && halfStar) starClass = "fas fa-star-half-alt checked";

            const star = document.createElement("i");
            star.className = starClass;
            starContainer.appendChild(star);
        }

        // Cập nhật thanh tiến trình
        const ratingBreakdown = document.getElementById("rating-breakdown");
        ratingBreakdown.innerHTML = "";
        const total = data.totalReviews;

        for (let i = 5; i >= 1; i--) {
            let percentage = total ? (data.stars[i] / total) * 100 : 0;
            let progressBar = `
<div class="d-flex align-items-center mt-2">
    <span class="me-2">${i}<i class="fas fa-star check"></i></span>
    <div class="progress flex-grow-1" style="height: 10px;">
        <div class="progress-bar" style="width: ${percentage}%"></div>
    </div>
    <span class="ms-2">${data.stars[i]}</span>
</div>`;
            ratingBreakdown.innerHTML += progressBar;
        }

        // Cập nhật vòng tròn phần trăm
        let isRecommended = data.isRecommended;
        let isRecommendedPercentage = total ? Math.round((isRecommended / total) * 100) : 0;
        document.getElementById("circle").style.setProperty("--percent", `${isRecommendedPercentage}%`);
        document.getElementById("circle").textContent = `${isRecommendedPercentage}%`;
    }

    $.get(`/api/companies/${companyId}/rating`, function (response) {
        // Dữ liệu đánh giá từ API (giả lập)
        const ratingData = response.ratingDetails;
        updateRatingList(ratingData);
    })
    function updateRatingList(ratingData) {
        // Hiển thị danh sách đánh giá
        const ratingList = $("#rating-list");
        ratingData.forEach(item => {
            const starIcons = getStarIcons(item.avg);
            const ratingItem = `
    <div class="rating-list-item row" data-id="${item.id}">
        <div class="col-6">
            <p>${item.name}</p>
        </div>
        <div class="col-6 text-end">
            <span class="star-rating">${starIcons} ${item.avg.toFixed(1)}</span>
        </div>
    </div>
`;
            ratingList.append(ratingItem);
        });
        
    }
    // Xử lý nút Show more và Show less
    $(".show-more-btn").click(function () {
        $(".rating-container").addClass("visible");
        $(this).hide();
        $(".show-less-btn").show();
    });

    $(".show-less-btn").click(function () {
        $(".rating-container").removeClass("visible");
        $(this).hide();
        $(".show-more-btn").show();
    });
    $(".show-more-job").click(function () {
        $(".extra-row").removeClass("d-none");
        $(this).hide();
        $(".show-less-job").show();
    });

    $(".show-less-job").click(function () {
        $(".extra-row").addClass("d-none");
        $(this).hide();
        $(".show-more-job").show();
    });

    // Hàm tạo icon ngôi sao dựa trên số trung bình
    function getStarIcons(avg) {
        let fullStars = Math.floor(avg);
        let halfStar = avg % 1 >= 0.5;
        let starIcons = "";

        for (let i = 1; i <= 5; i++) {
            if (i <= fullStars) {
                starIcons += '<i class="fas fa-star checked"></i> ';
            } else if (i === fullStars + 1 && halfStar) {
                starIcons += '<i class="fas fa-star-half-alt checked"></i> ';
            } else {
                starIcons += '<i class="far fa-star"></i> ';
            }
        }
        return starIcons;
    }
});
