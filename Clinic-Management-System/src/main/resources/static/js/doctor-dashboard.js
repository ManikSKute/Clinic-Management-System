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

document.addEventListener('DOMContentLoaded', async () => {
    const role = localStorage.getItem('role');
    if (window.location.pathname.includes('doctor-dashboard.html') && role === 'ROLE_DOCTOR') {
        const response = await fetchWithAuth('/doctor/me');
        if (response.ok) {
            const doctorId = await response.json();
            localStorage.setItem('doctorId', doctorId);
            loadDoctorAppointments();
        }
    } else {
        window.location.href = '/login.html';
    }
});/**
 * 
 */