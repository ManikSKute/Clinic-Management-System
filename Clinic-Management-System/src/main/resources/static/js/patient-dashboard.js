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

async function loadPatientAppointments() {
    const tableBody = document.querySelector('#appointments-table tbody');
    if (!tableBody) return;
    const patientId = localStorage.getItem('patientId');
    if (!patientId) return;
    try {
        const response = await fetchWithAuth(`/patient/appointment/${patientId}`);
        const appointments = await response.json();
        tableBody.innerHTML = '';
        appointments.forEach(appointment => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${appointment.appointment_id}</td>
                <td>${appointment.doctor.doctor_id}</td>
                <td>${appointment.doctor.name}</td>
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

async function bookAppointment() {
    const doctorId = document.getElementById('doctor-id').value;
    const patientId = localStorage.getItem('patientId');
    const appointmentDate = document.getElementById('appointment-date').value;
    const appointmentTime = document.getElementById('appointment-time').value;
    const errorDiv = document.getElementById('appointment-error');
    const successDiv = document.getElementById('appointment-success');

    // Client-side validation for date and time (Fix issue 6)
    const today = new Date().toISOString().split('T')[0];
    if (appointmentDate < today) {
        errorDiv.textContent = 'Appointment date must be today or in the future.';
        successDiv.textContent = '';
        return;
    }

    try {
        const response = await fetchWithAuth(`/patient/appointment/book/${doctorId}/${patientId}`, {
            method: 'POST',
            body: JSON.stringify({ appointmentDate, appointmentTime }) // Updated field names
        });
        if (response.ok) {
            const appointment = await response.json();
            document.getElementById('doctor-id').value = '';
            document.getElementById('appointment-date').value = '';
            document.getElementById('appointment-time').value = '';
            errorDiv.textContent = '';
            successDiv.innerHTML = `
                <p>Appointment added successfully!</p>
                <p><strong>Appointment Details:</strong></p>
                <p>Appointment ID: ${appointment.appointment_id}</p>
                <p>Patient Name: ${appointment.patient.name}</p>
                <p>Doctor Name: ${appointment.doctor.name}</p>
                <p>Date: ${appointment.appointment_date}</p>
                <p>Time: ${appointment.appointment_time}</p>
                <p>Status: ${appointment.status}</p>
            `;
        } else {
            errorDiv.textContent = await response.text();
            successDiv.textContent = '';
        }
    } catch (error) {
        errorDiv.textContent = 'Error booking appointment.';
        successDiv.textContent = '';
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    const role = localStorage.getItem('role');
    if (window.location.pathname.includes('patient-dashboard.html') && role === 'ROLE_PATIENT') {
        const response = await fetchWithAuth('/patient/me');
        if (response.ok) {
            const patientId = await response.json();
            localStorage.setItem('patientId', patientId);
            loadPatientDoctors();
            loadPrescriptions();
            loadPatientAppointments();
        }
    } else {
        window.location.href = '/login.html';
    }
});