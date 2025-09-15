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
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = '/login.html';
}

// Admin Dashboard Functions
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

// Doctor Dashboard Functions
async function loadDoctorAppointments() {
    const tableBody = document.querySelector('#appointments-table tbody');
    if (!tableBody) return;
    try {
        const response = await fetchWithAuth(`/doctor/appointments/${localStorage.getItem('doctorId')}`);
        const appointments = await response.json();
        tableBody.innerHTML = '';
        appointments.forEach(appointment => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${appointment.appointment_id}</td>
                <td>${appointment.patient.patient_id}</td>
                <td>${appointment.appointment_date}</td>
                <td>${appointment.appointment_time}</td>
                <td>${appointment.status}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

async function addPrescription() {
    const appointment_id = document.getElementById('appointment-id').value;
    const patient_id = document.getElementById('patient-id').value;
    const doctor_id = document.getElementById('doctor-id').value;
    const details = document.getElementById('prescription-details').value;
    const errorDiv = document.getElementById('prescription-error');

    try {
        const response = await fetchWithAuth('/doctor/prescription/add', {
            method: 'POST',
            body: JSON.stringify({ appointment_id, patient_id, doctor_id, details })
        });
        if (response.ok) {
            document.getElementById('appointment-id').value = '';
            document.getElementById('patient-id').value = '';
            document.getElementById('doctor-id').value = '';
            document.getElementById('prescription-details').value = '';
        } else {
            errorDiv.textContent = await response.text();
        }
    } catch (error) {
        errorDiv.textContent = 'Error adding prescription.';
    }
}

// Patient Dashboard Functions
async function loadPatientDoctors() {
    const tableBody = document.querySelector('#doctors-table tbody');
    if (!tableBody) return;
    try {
        const response = await fetchWithAuth('/patient/doctors');
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
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

async function loadPrescriptions() {
    const tableBody = document.querySelector('#prescriptions-table tbody');
    if (!tableBody) return;
    try {
        const response = await fetchWithAuth(`/patient/prescriptions/${localStorage.getItem('patientId')}`);
        const prescriptions = await response.json();
        tableBody.innerHTML = '';
        prescriptions.forEach(prescription => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${prescription.prescription_id}</td>
                <td>${prescription.appointment.appointment_id}</td>
                <td>${prescription.doctor.doctor_id}</td>
                <td>${prescription.details}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading prescriptions:', error);
    }
}

async function bookAppointment() {
    const doctorId = document.getElementById('doctor-id').value;
    const patientId = localStorage.getItem('patientId');
    const errorDiv = document.getElementById('appointment-error');

    try {
        const response = await fetchWithAuth(`/patient/appointment/book/${doctorId}/${patientId}`, {
            method: 'POST'
        });
        if (response.ok) {
            document.getElementById('doctor-id').value = '';
        } else {
            errorDiv.textContent = await response.text();
        }
    } catch (error) {
        errorDiv.textContent = 'Error booking appointment.';
    }
}

// Receptionist Dashboard Functions
async function loadAllAppointments() {
    const tableBody = document.querySelector('#appointments-table tbody');
    if (!tableBody) return;
    try {
        const response = await fetchWithAuth('/receptionist/appointments');
        const appointments = await response.json();
        tableBody.innerHTML = '';
        appointments.forEach(appointment => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${appointment.appointment_id}</td>
                <td>${appointment.patient.patient_id}</td>
                <td>${appointment.doctor.doctor_id}</td>
                <td>${appointment.appointment_date}</td>
                <td>${appointment.appointment_time}</td>
                <td>${appointment.status}</td>
                <td>
                    <select onchange="updateAppointmentStatus(${appointment.appointment_id}, this.value)">
                        <option value="BOOKED" ${appointment.status === 'BOOKED' ? 'selected' : ''}>Booked</option>
                        <option value="CANCELLED" ${appointment.status === 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                        <option value="COMPLETED" ${appointment.status === 'COMPLETED' ? 'selected' : ''}>Completed</option>
                    </select>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

async function updateAppointmentStatus(id, status) {
    try {
        const response = await fetchWithAuth(`/receptionist/appointment/${id}/status?status=${status}`, {
            method: 'PUT'
        });
        if (response.ok) {
            loadAllAppointments();
        }
    } catch (error) {
        console.error('Error updating appointment status:', error);
    }
}

// Load dashboard data based on role
document.addEventListener('DOMContentLoaded', async () => {
    const role = localStorage.getItem('role');
    if (window.location.pathname.includes('admin-dashboard.html') && role === 'ROLE_ADMIN') {
        loadDoctors();
        loadPatients();
    } else if (window.location.pathname.includes('doctor-dashboard.html') && role === 'ROLE_DOCTOR') {
        // Fetch doctor ID (assumes endpoint to get current doctor's ID)
        const response = await fetchWithAuth('/doctor/me');
        if (response.ok) {
            const doctorId = await response.json();
            localStorage.setItem('doctorId', doctorId);
            loadDoctorAppointments();
        }
    } else if (window.location.pathname.includes('patient-dashboard.html') && role === 'ROLE_PATIENT') {
        const response = await fetchWithAuth('/patient/me');
        if (response.ok) {
            const patientId = await response.json();
            localStorage.setItem('patientId', patientId);
            loadPatientDoctors();
            loadPrescriptions();
        }
    } else if (window.location.pathname.includes('receptionist-dashboard.html') && role === 'ROLE_RECEPTIONIST') {
        loadAllAppointments();
    } else {
        window.location.href = '/login.html';
    }
});