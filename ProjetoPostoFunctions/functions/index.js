const functions = require('firebase-functions');
var admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.notify = functions.database.ref('/promotions/{pushId}').onCreate(event => {
	//Utiliza event.data para obter os dados que foram inseridos no database.
	//Depois pega cada child do eventSnap para poder utilizar na formação da notificação.
	const posto_name = event.data.child('posto_nome').val();
	const posto_localizacao = event.data.child('posto_localizacao').val();
	const posto_comb1 = event.data.child('posto_comb1').val();
	const posto_comb2 = event.data.child('posto_comb2').val();
	const posto_valor_comb1 = event.data.child('posto_valor_comb1').val();
	const posto_valor_comb2 = event.data.child('posto_valor_comb2').val();

	console.log(posto_name);
	console.log(posto_localizacao);

	//Formação da Mensagem a ser enviada com a promoção
	var mensagemBody;
	if(posto_comb1 != 'Selecione'){
		if(posto_comb2 == 'Selecione'){
			mensagemBody = `${posto_comb1} = ${posto_valor_comb1}. Endereço: ${posto_localizacao}`;
		} else {
			mensagemBody = `${posto_comb1} = ${posto_valor_comb1},
        	   				${posto_comb2} = ${posto_valor_comb2}. Endereço: ${posto_localizacao}`
		}
	} else {
		mensagemBody = `${posto_comb2} = ${posto_valor_comb2}. Endereço: ${posto_localizacao}`;
	}
	const payload = {
      notification: {
        title: `Promoção no ${posto_name}`,
        body: mensagemBody
      }
    };

    return Promise.resolve(sendMessages(payload));
});

    

function sendMessages(payload){
	var topic = 'MessageTopic';
	// Send notifications to all tokens.
	return admin.messaging().sendToTopic(topic, payload)
	.catch(error => console.log('Error: ', error));
}
