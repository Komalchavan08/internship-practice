/**
 * Shared form-field validators.
 * Used by signup.js, addStudent.js and editStudent.js so the validation
 * rules only live in one place instead of being copy-pasted per page.
 */

const Validators = {
    name(value) {
        if (value.length < 3) {
            return { valid: false, message: "Name must contain at least 3 characters." };
        }
        if (!/^[A-Za-z ]+$/.test(value)) {
            return { valid: false, message: "Only alphabets are allowed." };
        }
        return { valid: true };
    },

    email(value) {
        const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!pattern.test(value)) {
            return { valid: false, message: "Enter a valid email address." };
        }
        return { valid: true };
    },

    password(value) {
        if (value.length < 6) {
            return { valid: false, message: "Password must contain at least 6 characters." };
        }
        return { valid: true };
    },

    department(value) {
        if (value.length < 2) {
            return { valid: false, message: "Department is required." };
        }
        if (!/^[A-Za-z ]+$/.test(value)) {
            return { valid: false, message: "Only alphabets are allowed." };
        }
        return { valid: true };
    },

    city(value) {
        if (value.length < 2) {
            return { valid: false, message: "City is required." };
        }
        if (!/^[A-Za-z ]+$/.test(value)) {
            return { valid: false, message: "Only alphabets are allowed." };
        }
        return { valid: true };
    },

    age(value) {
        const age = parseInt(value, 10);
        if (isNaN(age) || age < 18 || age > 60) {
            return { valid: false, message: "Age must be between 18 and 60." };
        }
        return { valid: true };
    },

    course(value) {
        if (value.length < 2) {
            return { valid: false, message: "Course is required." };
        }
        if (!/^[A-Za-z ]+$/.test(value)) {
            return { valid: false, message: "Only alphabets are allowed." };
        }
        return { valid: true };
    },

    mobile(value) {
        if (!/^[0-9]{10}$/.test(value)) {
            return { valid: false, message: "Enter a valid 10-digit mobile number." };
        }
        return { valid: true };
    },

    dob(value) {
        if (!value) {
            return { valid: false, message: "Date of birth is required." };
        }
        const dob = new Date(value);
        const age = (Date.now() - dob.getTime()) / (1000 * 60 * 60 * 24 * 365.25);
        if (dob > new Date() || age > 100) {
            return { valid: false, message: "Enter a valid date of birth." };
        }
        return { valid: true };
    },

    address(value) {
        if (value.length < 5) {
            return { valid: false, message: "Address is required." };
        }
        return { valid: true };
    }
};

/**
 * Wires one input to its error <small> element and a validator function.
 * Returns a function that re-runs the validation on demand (used before submit).
 */
function attachValidator(input, errorEl, validatorFn) {
    const run = () => {
        const value = input.value.trim();
        const result = validatorFn(value);

        if (!result.valid) {
            errorEl.innerHTML = result.message;
            input.classList.add("invalid");
            input.classList.remove("valid");
        } else {
            errorEl.innerHTML = "";
            input.classList.remove("invalid");
            input.classList.add("valid");
        }

        return result.valid;
    };

    input.addEventListener("input", run);
    return run;
}