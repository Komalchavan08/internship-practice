function login(){

    const email = document.getElementById("email").value.trim();

    const password = document.getElementById("password").value.trim();

    if(email==="" || password===""){

        document.getElementById("message").style.color="red";

        document.getElementById("message").innerHTML="Please enter email and password.";

        return;
    }

    const student = {

        email,
        password

    };

    fetch("http://localhost:8081/students/login",{

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify(student)

    })

    .then(response=>response.text())

    .then(data=>{

        if(data==="Login Successful"){

            localStorage.setItem("loggedIn","true");

            document.getElementById("message").style.color="green";

            document.getElementById("message").innerHTML=data;

            setTimeout(function(){

                window.location.href="dashboard.html";

            },1000);

        }else{

            document.getElementById("message").style.color="red";

            document.getElementById("message").innerHTML=data;
        }

    });

}