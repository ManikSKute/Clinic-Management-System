async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login.html';
        return;
    }
    options.headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
    const response = await fetch(url, options);
    if (response.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        window.location.href = '/login.html';
    }
    return response;
}

function handleLogout() {
    localStorage.clear();
    window.location.href = '/login.html';
}

async function loadDoctors() {
    const tableBody = document.querySelector('#doctors-table tbody');
    if (!tableBody) return;
    try {
        const response = await fetchWithAuth('/admin/doctors');
        const doctors = await response.json();
        tableBody.innerHTML = '';
        doctors.forEach(doctor => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${doctor.doctor_id}</td>
                <td>${doctor.name}</td>
                <td>${doctor.specialization || ''}</td>
                <td>${doctor.contact || ''}</td>
                <td>${doctor.availability || ''}</td>
                <td><button onclick="deleteDoctor(${doctor.doctor_id})">Delete</button></td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

async function loadPatients() {
    const tableBody = document.querySelector('#patients-table tbody');
    if (!tableBody) return;
    try {
        const response = await fetchWithAuth('/admin/patients');
        const patients = await response.json();
        tableBody.innerHTML = '';
        patients.forEach(patient => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${patient.patient_id}</td>
                <td>${patient.name}</td>
                <td>${patient.age || ''}</td>
                <td>${patient.gender || ''}</td>
                <td>${patient.contact || ''}</td>
                <td>${patient.address || ''}</td>
                <td><button onclick="deletePatient(${patient.patient_id})">Delete</button></td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading patients:', error);
    }
}

async function addDoctor() {
    const username = document.getElementById('doctor-username').value;
    const password = document.getElementById('doctor-password').value;
    const name = document.getElementById('doctor-name').value;
    const specialization = document.getElementById('doctor-specialization').value;
    const contact = document.getElementById('doctor-contact').value;
    const availability = document.getElementById('doctor-availability').value;
    const errorDiv = document.getElementById('doctor-error');

    try {
        const response = await fetchWithAuth('/admin/doctors', {
            method: 'POST',
            body: JSON.stringify({
                user: { username, password, role: 'ROLE_DOCTOR' },
                name, specialization, contact, availability
            })
        });
        if (response.ok) {
            loadDoctors();
            document.getElementById('doctor-username').value = '';
            document.getElementById('doctor-password').value = '';
            document.getElementById('doctor-name').value = '';
            document.getElementById('doctor-specialization').value = '';
            document.getElementById('doctor-contact').value = '';
            document.getElementById('doctor-availability').value = '';
        } else {
            errorDiv.textContent = await response.text();
        }
    } catch (error) {
        errorDiv.textContent = 'Error adding doctor.';
    }
}

async function addReceptionist() {
    const username = document.getElementById('receptionist-username').value;
    const password = document.getElementById('receptionist-password').value;
    const errorDiv = document.getElementById('receptionist-error');

    try {
        const response = await fetchWithAuth('/admin/receptionists', {
            method: 'POST',
            body: JSON.stringify({ username, password, role: 'ROLE_RECEPTIONIST' })
        });
        if (response.ok) {
            document.getElementById('receptionist-username').value = '';
            document.getElementById('receptionist-password').value = '';
        } else {
            errorDiv.textContent = await response.text();
        }
    } catch (error) {
        errorDiv.textContent = 'Error adding receptionist.';
    }
}

async function deleteDoctor(id) {
    try {
        const response = await fetchWithAuth(`/admin/doctor/${id}`, { method: 'DELETE' });
        if (response.ok) {
            loadDoctors();
        }
    } catch (error) {
        console.error('Error deleting doctor:', error);
    }
}

async function deletePatient(id) {
    try {
        const response = await fetchWithAuth(`/admin/patient/${id}`, { method: 'DELETE' });
        if (response.ok) {
            loadPatients();
        }
    } catch (error) {
        console.error('Error deleting patient:', error);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    const role = localStorage.getItem('role');
    if (window.location.pathname.includes('admin-dashboard.html') && role === 'ROLE_ADMIN') {
        loadDoctors();
        loadPatients();
    } else {
        window.location.href = '/login.html';
    }
});