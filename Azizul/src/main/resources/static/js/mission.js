const form = document.getElementById("missionForm");
const id = document.getElementById("missionId");
const nameInput = document.getElementById("name");
const agency = document.getElementById("agency");
const date = document.getElementById("date");
const orbit = document.getElementById("orbit");
const status = document.getElementById("status");

const title = document.getElementById("formTitle");
const submitBtn = document.getElementById("submitBtn");
const cancelBtn = document.getElementById("cancelBtn");

const deleteModal = document.getElementById("deleteModal");

let deleteForm = null;


function editMission(button) {

    id.value = button.dataset.id;
    nameInput.value = button.dataset.name;
    agency.value = button.dataset.agency;
    date.value = button.dataset.date;
    orbit.value = button.dataset.orbit;
    status.value = button.dataset.status;

    title.textContent = "Edit Mission";
    submitBtn.textContent = "Update Mission";

    cancelBtn.classList.remove("hidden");

    nameInput.focus();

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}


function cancelEdit() {

    form.reset();

    id.value = "";

    title.textContent = "Add New Mission";
    submitBtn.textContent = "Save Mission";

    cancelBtn.classList.add("hidden");
}


function confirmDelete(formElement) {

    deleteForm = formElement;

    deleteModal.classList.add("show");

    document.body.classList.add("modal-open");

    return false;
}


function closeDeleteModal() {

    deleteModal.classList.remove("show");

    document.body.classList.remove("modal-open");

    deleteForm = null;
}


function confirmMissionDelete() {

    if (deleteForm !== null) {
        deleteForm.submit();
    }
}


deleteModal.addEventListener("click", function(event) {

    if (event.target === deleteModal) {
        closeDeleteModal();
    }
});


document.addEventListener("keydown", function(event) {

    if (event.key === "Escape" && deleteModal.classList.contains("show")) {
        closeDeleteModal();
    }
});