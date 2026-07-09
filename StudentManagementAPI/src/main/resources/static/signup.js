function signup(){

    const studentName = document.getElementById("studentName").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const department = document.getElementById("department").value.trim();
    const city = document.getElementById("city").value.trim();
    const age = document.getElementById("age").value.trim();
    const course = document.getElementById("course").value.trim();

    if(studentName==="" || email==="" || password==="" ||
       department==="" || city==="" || age==="" || course===""){

        document.getElementById("message").style.color="red";
        document.getElementById("message").innerHTML="Please fill all fields.";

        return;
    }

    const student = {
        studentName,
        email,
        password,
        department,
        city,
        age,
        course
    };

    fetch("http://localhost:8081/students/signup",{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify(student)

    })

    .then(response=>response.text())

    .then(data=>{

        if(data==="Student Registered Successfully"){

            document.getElementById("message").style.color="green";

            document.getElementById("message").innerHTML=data;

            setTimeout(function(){

                window.location.href="login.html";

            },1500);

        }else{

            document.getElementById("message").style.color="red";

            document.getElementById("message").innerHTML=data;
        }

    });

}