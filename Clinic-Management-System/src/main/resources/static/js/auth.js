function clearErrors() {
    const errorDivs = [
        'login-error',
        'admin-register-error',
        'admin-register-success',
        'patient-register-error',
        'patient-register-success'
    ];
    errorDivs.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.textContent = '';
    });
}

// Parse JWT to extract role
function getRoleFromToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role;
    } catch (error) {
        console.error('Error parsing JWT:', error);
        return null;
    }
}


//Handle Patient Registration
async function handlePatientRegistration() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const name = document.getElementById('name').value;
    const age = document.getElementById('age').value;
    const gender = document.getElementById('gender').value;
    const contact = document.getElementById('contact').value;
    const address = document.getElementById('address').value;
    const medical_history = document.getElementById('medical_history').value;
    const errorDiv = document.getElementById('patient-register-error');
    const successDiv = document.getElementById('patient-register-success');

    try {
        const response = await fetch('/api/auth/register/patient', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                user: { username, password, role: 'ROLE_PATIENT' },
                name,
                age: age ? parseInt(age) : null,
                gender: gender || null,
                contact,
                address,
                medical_history
            })
        });

        if (response.ok) {
            successDiv.textContent = 'Registration successful! Redirecting to login...';
            setTimeout(() => { window.location.href = '/login.html'; }, 2000);
        } else {
            errorDiv.textContent = await response.text();
        }
    } catch (error) {
        errorDiv.textContent = 'Error during registration. Please try again.';
    }
}



//Handle Login
async function handleLogin() {
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    const errorDiv = document.getElementById('login-error');

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            const role = getRoleFromToken(token);
            if (role) {
                localStorage.setItem('token', token);
                localStorage.setItem('role', role);
                // Redirect to role-specific dashboard
                switch (role) {
                    case 'ROLE_ADMIN':
                        window.location.href = 'dashboard/admin-dashboard.html';
                        break;
                    case 'ROLE_DOCTOR':
                        window.location.href = 'dashboard/doctor-dashboard.html';
                        break;
                    case 'ROLE_PATIENT':
                        window.location.href = 'dashboard/patient-dashboard.html';
                        break;
                    case 'ROLE_RECEPTIONIST':
                        window.location.href = 'dashboard/receptionist-dashboard.html';
                        break;
                    default:
                        errorDiv.textContent = 'Unknown role.';
                }
            } else {
                errorDiv.textContent = 'Unable to determine user role.';
            }
        } else {
            errorDiv.textContent = await response.text();
        }
    } catch (error) {
        errorDiv.textContent = 'Error during login. Please try again.';
    }
}

// Clear errors on page load for login page
document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.includes('login.html')) {
        clearErrors();
    }
});