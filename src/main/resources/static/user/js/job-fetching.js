//Address search
// Add event listener for clicks on the document or a container
// Integrate event listener to <a> tag
function addAddressSearch() {
  $(".search-address").click(function (event) {
    event.preventDefault();
    const address = $(this).closest("p").find("span").text();
    if (address) {
      const googleMapsURL = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(
        address
      )}`;
      window.open(googleMapsURL, "_blank");
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  addAddressSearch();
});

document.addEventListener("click", function (event) {
  // Use closest to find the nearest .job-card ancestor, including itself
  const jobCard = event.target.closest(".job-card");
  if (jobCard?.classList?.contains("selected")) return;
  const previousSelected = document.querySelector(".job-card.selected");
  // Check if the click happened on or inside a .job-card
  if (jobCard) {
    const jobId = jobCard.getAttribute("data-job-id");
    jobCard.classList.add("selected");
    previousSelected.classList.remove("selected");
    fetchJobData(jobId);
  }
});

// Function to fetch job data based on job-id
async function fetchJobData(jobId) {
  try {
    const jobResponse = await fetch(`/api/job-json/${jobId}`);
    const likedResponse = await fetch(`/api/liked/${jobId}`);
    const appliedResponse = await fetch(`/api/applied/${jobId}`);
    const liked = await likedResponse.json();
    const job = await jobResponse.json();
    const applied = await appliedResponse.json();
    renderJobContent(job, liked, applied);
  } catch (error) {
    console.error("Error fetching job data:", error);
  }
}

// Function to render job content based on the fetched data
function renderJobContent(job, liked, applied) {
  const section1 = document.querySelector("#section-1");

  //Modify the content of the section
  const notSignedIn = document.querySelector("#not-signed-in") !== null;

  section1.innerHTML = `
    <div class="image-container">
      <img src="/company-logo/${
        job.company.logo
      }" alt="Company Logo" class="img-fluid rounded-circle">
    </div>
    <div class="text-start ps-4">
      <h4>${job.title}</h4>
      <p class="text-truncate me-3 mb-3">${job.company.name}</p>
      <p class="text-truncate me-3" style="font-size: 1.25rem; font-weight: bold;">
        <i class="far fa-money-bill-alt text-primary me-2"></i>
        ${
          notSignedIn
            ? '<a href="/login">Đăng nhập để xem mức lương</a>'
            : `<span>${job.salary}</span>`
        }
      </p>
    </div>
    <div class="text-start ps-4">
      <a href="jobs/${job.id}" target="_blank">
        <i class="fa fa-external-link-alt text-primary"></i>
      </a>
    </div>
`;

  const section2 = document.querySelector("#section-2");
  if (section2) {
    if (!applied) {
      section2.innerHTML = `
      <a href="/jobs/${
        job.id
      }/apply" class="btn btn-primary flex-grow-1 me-2">Ứng tuyển</a>
      <a class="btn btn-light" id="handle-add-delete-favorite-job" 
        data-job-id=${job.id} >
      ${
        liked
          ? `<i class="fa fa-heart text-primary"></i>`
          : `<i class="far fa-heart text-primary"></i>`
      }
      </a>
    `;
    } else {
      section2.innerHTML = `<div class="alert alert-success flex-grow-1 me-2" role="alert">
                      Bạn đã ứng tuyển công việc này. Bạn có thể ứng tuyển lại sau 7 ngày. <br />
                      Xem các công việc đã ứng tuyển
                      <a href="/cv-status" class="alert-link"><u>Tại đây</u></a>.
                    </div>`;
    }
  }

  const section3 = document.querySelector("#section-3");
  const daysAgo = Math.floor(
    (Date.now() - new Date(job.updatedAt)) / (1000 * 60 * 60 * 24)
  );

  section3.innerHTML = `
    ${job.availableBranches
      .map(
        (branch) => `
      <p class="text-truncate me-3">
        <i class="fa fa-map-marker-alt text-primary me-2"></i>
        <span>${branch.address}</span>
        <a href="#" class="search-address">
          <i class="fa fa-external-link-alt text-primary"></i>
        </a>
      </p>
    `
      )
      .join("")}
      <p class="text-truncate me-3"><i class="fa fa-briefcase text-primary me-2"></i>
        <span>${job.workingModel}</span>
      </p>

      <p class="text-truncate me-3"><i class="far fa-clock text-primary me-2"></i>
        Cập nhật <span>${daysAgo}</span> ngày trước
      </p>
      <div class="d-flex flex-wrap gap-2 mt-3">
        Kỹ năng:
        <!-- Tăng gap -->
      ${job.skills
        .map(
          (skill) => `
          <span data-skill-id=${skill.id} class="skill-tag badge bg-light text-dark rounded-pill px-3 py-2">${skill.name}</span>
        `
        )
        .join("")}
      </div>
  `;
  const section4 = document.querySelector("#section-4");
  section4.innerHTML = job.description;

  const section5 = document.querySelector("#section-5");
  section5.innerHTML = `
    <h5>${job.company.name}</h5>
    <div class="row">
      <div class="col-md-4 info-item">
        <span class="label">Mô hình công ty</span>
        <span class="value">${job.company.type}</span>
      </div>
      <div class="col-md-4 info-item">
        <span class="label">Lĩnh vực</span>
        <span class="value">${job.company.industry}</span>
      </div>
      <div class="col-md-4 info-item">
        <span class="label">Quốc gia</span>
        <span class="value">${job.company.country}</span>
      </div>
      <div class="col-md-4 info-item mt-3">
        <span class="label">Quy mô</span>
        <span class="value">${job.company.size}</span>
      </div>
      <div class="col-md-4 info-item mt-3">
        <span class="label">Ngày làm việc</span>
        <span class="value">Thứ 2 - Thứ 6</span>
      </div>
      <div class="col-md-4 info-item mt-3">
        <span class="label">Chính sách làm thêm giờ</span>
        <span class="value">${job.company.otPolicy}</span>
      </div>
    </div>
  `;
  addAddressSearch();
  addSkillSearch();
}
