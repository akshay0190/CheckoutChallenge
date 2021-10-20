const clientKey = "test_7ZCG2HRVQ5DYDG54CTVFVXKYFIKWXTBF";
async function initCheckout() {
  try {
    const paymentMethodsResponse = await callServer("/api/getPaymentMethods");
        const configuration = {
         paymentMethodsResponse: paymentMethodsResponse, // The `/paymentMethods` response from the server.
         clientKey: clientKey, // Web Drop-in versions before 3.10.1 use originKey instead of clientKey.
         locale: "en-US",
         environment: "test",
         showPayButton: true,
         onSubmit: (state, component) => {

            if (state.isValid) {
                      handleSubmission(state, component, "/api/startPayment");
                     }
           },
         onAdditionalDetails: (state, component) => {

         },
         paymentMethodsConfiguration: {
            ideal:{
            showImage:true,
            },
            card: {
             hasHolderName: true,
             holderNameRequired: true,
             enableStoreDetails: true,
             hideCVC: false,
             name: 'Credit or debit card',
             amount:{
                    value: 20000,
                    currency: "EUR",
             },
             onSubmit: (state, component) => {
             if (state.isValid) {
                              handleSubmission(state, component, "/api/startPayment");
                              }

             },
           },
         }
        };
       const checkout = new AdyenCheckout(configuration);
         checkout.create("dropin").mount(document.getElementById("dropin-container"));
    }catch (error) {
    console.error(error);
    alert("Error occurred.");
  }
  }
async function handleSubmission(state, component, url) {
  try {
    const res = await callServer(url, state.data);
    handleServerResponse(res, component);
  } catch (error) {
    console.error(error);
    alert("Error occurred.");
  }
}

function handleServerResponse(res, component) {
  if (res.action) {
    component.handleAction(res.action);
  }
  else {
      switch (res.resultCode) {
        case "Authorised":
          window.location.href = "/completed/success";
          break;
        case "Pending":
        case "Received":
          window.location.href = "/completed/pending";
          break;
        case "Refused":
          window.location.href = "/completed/failed";
          break;
        default:
          window.location.href = "/completed/error";
          break;
      }
    }
  }


  async function callServer(url, data) {
    const res = await fetch(url, {
      method: "POST",
      body: data ? JSON.stringify(data) : "",
      headers: {
        "Content-Type": "application/json",
      },
    });

    return await res.json();
  }
  initCheckout();
