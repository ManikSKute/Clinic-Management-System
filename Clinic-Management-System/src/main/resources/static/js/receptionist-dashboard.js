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
                <td>${appointment.patient.name}</td>
                <td>${appointment.doctor.name}</td>
                <td>${appointment.appointment_date}</td>
                <td>${appointment.appointment_time}</td>
                <td>${appointment.status}</td>
                <td>
                    <select onchange="updateAppointmentStatus(${appointment.appointment_id}, this.value)">
                        <option value="PENDING" ${appointment.status === 'PENDING' ? 'selected' : ''}>PENDING</option>
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

document.addEventListener('DOMContentLoaded', async () => {
    const role = localStorage.getItem('role');
    if (window.location.pathname.includes('receptionist-dashboard.html') && role === 'ROLE_RECEPTIONIST') {
        loadAllAppointments();
    } else {
        window.location.href = '/login.html';
    }
});