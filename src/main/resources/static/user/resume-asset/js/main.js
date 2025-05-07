// Function to format month input as mm/YYYY
function formatMonthInput(input) {
    const [year, month] = input.split("-");
    return `${month}/${year}`;
}

// Function to count elements by ID pattern
function countElementsByIdPattern(prefix) {
    const elements = document.querySelectorAll(`[id^="${prefix}"]`);
    let count = 0;

    elements.forEach(el => {
        const idSuffix = el.id.slice(prefix.length); // Extract the part after the prefix
        if (/^\d+$/.test(idSuffix)) { // Check if it consists only of numbers
            count++;
        }
    });

    return count;
}

let skillCount = 0;
let index = 1;

// Function to update value from input to output element
const updateValue = (inputId, outputId) => {
    const inputElement = document.getElementById(inputId);
    const outputElement = document.getElementById(outputId);
    if (inputElement && outputElement) {
        inputElement.addEventListener("input", () => {
            outputElement.textContent = inputElement.value;
        });
    }
};

// Function to update dynamic values based on input prefix and display prefix
function updateDynamicValue(inputPrefix, displayPrefix) {
    document.addEventListener("input", function (event) {
        const target = event.target;

        if (target.id.startsWith(inputPrefix)) {
            const index = target.id.replace(inputPrefix, ""); // Extract the number suffix
            const displayElement = document.getElementById(displayPrefix + index);

            if (displayElement) {
                displayElement.textContent = target.value;
            }
        }
    });
}

// Function to setup education section
function setupEducation() {
    document.getElementById("addEducation").addEventListener("click", function () {
        if (countElementsByIdPattern("education-") > 4) {
            alert("Học vấn chỉ được tối đa 5 mục thôi nhé");
            return;
        }
        const originalEducationInput = document.getElementById("education-0");
        const originalEducationDisplay = document.getElementById("education-display-0");

        const cloneEducationInput = originalEducationInput.cloneNode(true);
        const cloneEducationDisplay = originalEducationDisplay.cloneNode(true);

        cloneEducationInput.id = `education-${index}`;
        cloneEducationDisplay.id = `education-display-${index}`;

        cloneEducationInput.querySelectorAll("input, textarea").forEach(input => {
            input.id = input.id.replace('0', `${index}`);
            input.value = "";
            if (input.type === "month") {
                input.addEventListener("change", function () {
                    const displayElement = document.getElementById(input.id.replace("input", "display"));
                    displayElement.textContent = formatMonthInput(input.value);
                });
            }
        });

        // Add remove button
        const removeButton = document.createElement("button");
        removeButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
        removeButton.className = "btn btn-danger";
        removeButton.style.background = "none";
        removeButton.style.border = "none";
        removeButton.style.color = "red";
        removeButton.addEventListener("click", function () {
            cloneEducationInput.remove();
            cloneEducationDisplay.remove();
        });
        cloneEducationInput.appendChild(removeButton);

        cloneEducationDisplay.querySelectorAll('[id^="education-"][id$="-display-0"]').forEach(display => {
            display.id = display.id.replace('0', `${index}`);
            display.textContent = "";
        });
        cloneEducationInput.style.display = "block";
        cloneEducationDisplay.style.display = "block";
        originalEducationInput.parentNode.appendChild(cloneEducationInput);
        originalEducationDisplay.parentNode.appendChild(cloneEducationDisplay);

        index++;
    });
}

// Function to setup experience section
function setupExperience() {
    document.getElementById("addExperience").addEventListener("click", function () {
        const originalExperienceInput = document.getElementById("experience-0");
        const originalExperienceDisplay = document.getElementById("experience-display-0");

        const cloneExperienceInput = originalExperienceInput.cloneNode(true);
        const cloneExperienceDisplay = originalExperienceDisplay.cloneNode(true);

        cloneExperienceInput.id = `experience-${index}`;
        cloneExperienceDisplay.id = `experience-display-${index}`;

        cloneExperienceInput.querySelectorAll("input, textarea").forEach(input => {
            input.id = input.id.replace('0', `${index}`);
            input.value = "";
            if (input.type === "month") {
                input.addEventListener("change", function () {
                    const displayElement = document.getElementById(input.id.replace("input", "display"));
                    displayElement.textContent = formatMonthInput(input.value);
                });
            }
        });

        // Add remove button
        const removeButton = document.createElement("button");
        removeButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
        removeButton.className = "btn btn-danger";
        removeButton.style.background = "none";
        removeButton.style.border = "none";
        removeButton.style.color = "red";
        removeButton.addEventListener("click", function () {
            cloneExperienceInput.remove();
            cloneExperienceDisplay.remove();
        });
        cloneExperienceInput.appendChild(removeButton);

        cloneExperienceDisplay.querySelectorAll('[id^="experience-"][id$="-display-0"]').forEach(display => {
            display.id = display.id.replace('0', `${index}`);
            display.textContent = "";
        });
        cloneExperienceInput.style.display = "block";
        cloneExperienceDisplay.style.display = "block";
        originalExperienceInput.parentNode.appendChild(cloneExperienceInput);
        originalExperienceDisplay.parentNode.appendChild(cloneExperienceDisplay);

        index++;
    });
}

// Function to setup project section
function setupProject() {
    document.getElementById("addProject").addEventListener("click", function () {
        const originalprojectInput = document.getElementById("project-0");
        const originalprojectDisplay = document.getElementById("project-display-0");

        const cloneprojectInput = originalprojectInput.cloneNode(true);
        const cloneprojectDisplay = originalprojectDisplay.cloneNode(true);

        cloneprojectInput.id = `project-${index}`;
        cloneprojectDisplay.id = `project-display-${index}`;

        cloneprojectInput.querySelectorAll("input, textarea").forEach(input => {
            input.id = input.id.replace('0', `${index}`);
            input.value = "";
            if (input.type === "month") {
                input.addEventListener("change", function () {
                    const displayElement = document.getElementById(input.id.replace("input", "display"));
                    displayElement.textContent = formatMonthInput(input.value);
                });
            }
        });

        // Add remove button
        const removeButton = document.createElement("button");
        removeButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
        removeButton.className = "btn btn-danger";
        removeButton.style.background = "none";
        removeButton.style.border = "none";
        removeButton.style.color = "red";
        removeButton.addEventListener("click", function () {
            cloneprojectInput.remove();
            cloneprojectDisplay.remove();
        });
        cloneprojectInput.appendChild(removeButton);

        cloneprojectDisplay.querySelectorAll('[id^="project-"][id$="-display-0"]').forEach(display => {
            display.id = display.id.replace('0', `${index}`);
            display.textContent = "";
        });

        cloneprojectInput.style.display = "block";
        cloneprojectDisplay.style.display = "block";
        originalprojectInput.parentNode.appendChild(cloneprojectInput);
        originalprojectDisplay.parentNode.appendChild(cloneprojectDisplay);

        index++;
    });
}

// Function to handle file input trigger
function triggerFileInput() {
    document.querySelector('.file-input').click();
}

// Function to change image when a new file is selected
function changeImage(event) {
    const file = event.target.files[0];

    if (!file) return; // If no file is selected, exit early

    const fileType = file.type.toLowerCase(); // Get file type
    const validTypes = ["image/jpeg", "image/jpg", "image/png"]; // Accept only JPG, JPEG, PNG

    if (!validTypes.includes(fileType)) {
        alert("Vui lòng chọn file ảnh có định dạng JPG, JPEG hoặc PNG.");
        return;
    }
    var reader = new FileReader();
    reader.onload = function (e) {
        document.querySelector('.avatar-img').src = e.target.result;
    }
    reader.readAsDataURL(event.target.files[0]);
}

// Function to handle PDF download
document.getElementById("download-now").addEventListener("click", function () {
    const iframe = document.getElementById("pdf-preview");
    const pdfUrl = iframe.dataset.uri || iframe.src; // Get URL from dataset.uri or src

    let fileName = "my_resume";
    if (document.getElementById("file-name").value !== "") {
        fileName = document.getElementById("file-name").value;
    }
    console.log(fileName);

    if (pdfUrl) {
        const link = document.createElement("a");
        link.href = pdfUrl;
        link.download = fileName; // Set download file name
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } else {
        alert("Không tìm thấy file PDF!");
    }
});

// Function to handle PDF generation
document.getElementById('download-pdf').addEventListener('click', function () {
    const element = document.querySelector('.active'); // Select the part to export as PDF
    const images = element.getElementsByTagName('img');
    const imgType = 'jpeg';

    // Loop through each image and set the type dynamically based on its file extension
    for (let img of images) {
        const imgSrc = img.src.toLowerCase();
        if (imgSrc.endsWith('.png')) {
            imgType = 'png';
        }
    }

    html2pdf().from(element).set({
        margin: [5, 5, 5, 5],
        image: { type: imgType, quality: 1 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' },
        pagebreak: { mode: ['css', 'legacy'] } // Ensures content splits properly
    }).toPdf()
        .get('pdf')
        .then(function (pdf) {
            const pdfBlob = pdf.output('blob');
            const pdfUrl = URL.createObjectURL(pdfBlob);

            const pdfPreview = document.getElementById("pdf-preview");
            pdfPreview.src = pdfUrl;
            pdfPreview.dataset.url = pdfUrl;

            new bootstrap.Modal(document.getElementById('pdfModal')).show();
        });
});


// Function to add skill
const addSkill = () => {
    const skillSelect = document.getElementById('skill-name');
    const selectedSkill = skillSelect.value;
    if (selectedSkill) {
        skillCount++;
        const skillId = `skill-input-view-${skillCount}`;
        const skillDisplay = document.createElement('span');
        skillDisplay.id = skillId;
        skillDisplay.className = 'skill-item';
        skillDisplay.textContent = selectedSkill;

        // Add remove button
        const removeButton = document.createElement("button");
        removeButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
        removeButton.className = "btn btn-danger";
        removeButton.style.background = "none";
        removeButton.style.border = "none";
        removeButton.style.color = "red";
        
        skillDisplay.appendChild(removeButton);

        document.getElementById('skills-container').appendChild(skillDisplay);
        skillSelect.value = ''; // Reset the select element

        // Add new <li> to the CV preview
        const skillList = document.querySelector('.keySkills');
        const newSkillItem = document.createElement('li');
        newSkillItem.id = `skill-name-display-${skillCount}`;
        newSkillItem.textContent = selectedSkill;
        newSkillItem.contentEditable = true;
        skillList.appendChild(newSkillItem);
        removeButton.addEventListener("click", function () {
            skillDisplay.remove();
            newSkillItem.remove();
        });
    }
};

document.addEventListener("DOMContentLoaded", () => {
    // Map initial input fields to their display elements
    updateValue("fullname", "name-input");
    updateValue("title", "title-input");
    updateValue("address", "address-input");
    updateValue("email", "email-input");
    updateValue("phone", "phone-input");
    updateValue("web", "web-input");
    updateValue("introduction", "introduction-input");

    updateDynamicValue("education-name-input-", "education-name-display-");
    updateDynamicValue("education-date-from-input-", "education-date-from-display-");
    updateDynamicValue("education-date-to-input-", "education-date-to-display-");
    updateDynamicValue("education-detail-input-", "education-detail-display-");

    updateDynamicValue("experience-title-input-", "experience-title-display-");
    updateDynamicValue("experience-name-input-", "experience-name-display-");
    updateDynamicValue("experience-date-from-input-", "experience-date-from-display-");
    updateDynamicValue("experience-date-to-input-", "experience-date-to-display-");
    updateDynamicValue("experience-detail-input-", "experience-detail-display-");

    updateDynamicValue("project-name-input-", "project-name-display-");
    updateDynamicValue("project-date-from-input-", "project-date-from-display-");
    updateDynamicValue("project-date-to-input-", "project-date-to-display-");
    updateDynamicValue("project-detail-input-", "project-detail-display-");

    setupEducation();
    setupExperience();
    setupProject();

    document.getElementById('addSkill').addEventListener('click', addSkill);

    const colorOptions = document.querySelectorAll(".color-option");
    const mainDetails = document.querySelector(".mainDetails");
    const sectionTitles = document.querySelectorAll(".sectionTitle h1");

    colorOptions.forEach(option => {
        // Set each circle's background from data-color (for a visual preview)
        const colorValue = option.getAttribute("data-color");
        option.style.backgroundColor = colorValue;

        // Handle click event
        option.addEventListener("click", () => {
            // Change background of .mainDetails
            if (mainDetails) {
                mainDetails.style.backgroundColor = colorValue;
            }

            // Change color of all .sectionTitle h1
            sectionTitles.forEach(title => {
                title.style.color = colorValue;
            });
        });
    });
});