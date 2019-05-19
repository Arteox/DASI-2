/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 function verifierSession(){           
    $.ajax({
        url: 'ActionServlet', // URL
        method: 'POST',         // Méthode
        data: {                 // Paramètres
            todo: 'verificationSession'
        },
        dataType: 'json'        // Type de retour attendu
        }).done( function (response) {  // Appel OK => "response" contient le resultat JSON
            if(!response.connecte){
                //Utilisateur non connecté, redirection vers la page d'accueil
                window.location = "index.html";
            }
        });
}
            
