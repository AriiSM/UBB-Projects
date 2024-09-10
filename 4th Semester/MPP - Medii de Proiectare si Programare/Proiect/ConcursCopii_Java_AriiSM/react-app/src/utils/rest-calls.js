import {BASE_URL} from "./constants.js";

function status(response){
    console.log('response status '+response.status);
    if(response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else{
        return Promise.reject(new Error(response.statusText));
    }
}

function json(response){
    return response.json();
}

export function GetConcurs(){
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    let myInit = { method: 'GET',
        headers: headers,
        mode: 'cors'
    };
    let request = new Request(BASE_URL, myInit);
    console.log('Before GET for:  '+BASE_URL);
    return fetch(request)
        .then(status)
        .then(json)
        .then(data=> {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(error=>{
            console.log('Request failed', error);
            return Promise.reject(error);
        });
}


export function DeleteConcurs(id){
    console.log('Before delete');
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    let antet = { method: 'DELETE',
        headers: myHeaders,
        mode: 'cors'
    };
    const concursDelUrl=BASE_URL+'/'+id;
    console.log('URL for delete   '+concursDelUrl);
    return fetch(concursDelUrl,antet)
        .then(status)
        .then(response=>{
            console.log('Delete status '+response.status);
            return response.text();
        }).catch(e=>{
            console.log('error '+e);
            return Promise.reject(e);
        });
}

export function AddConcurs(concurs){
    console.log('Before add');
    console.log(concurs);
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");

    let antet = { method: 'POST',
        headers: myHeaders,
        body: JSON.stringify(concurs),
        mode: 'cors'
    };

    return fetch(BASE_URL, antet)
        .then(status)
        .then(response=>{
            console.log('Add status '+response.status);
            return response.text();
        }).catch(e=>{
            console.log('error '+e);
            return Promise.reject(e);
        });
}


export function EditConcurs(concurs){
    console.log('Before edit');
    console.log(concurs);
    let myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");

    let antet = { method: 'PUT',
        headers: myHeaders,
        body: JSON.stringify(concurs),
        mode: 'cors'
    };

    const concursEditUrl = BASE_URL + '/' + concurs.id;
    console.log('URL for edit ' + concursEditUrl);

    return fetch(concursEditUrl, antet)
        .then(status)
        .then(response=>{
            console.log('Edit status '+response.status);
            return response.text();
        }).catch(e=>{
            console.log('error '+e);
            return Promise.reject(e);
        });
}
