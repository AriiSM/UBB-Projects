import React, {useEffect, useState} from 'react'
import {AddConcurs, DeleteConcurs, GetConcurs, EditConcurs} from "./utils/rest-calls.js"
import ConcursRow from "./ConcursRow.jsx"
import ConcursForm from "./ConcursForm.jsx"

export default function UserApp(){
    const [concurses, setConcurses] = useState([{"Concurs":0,"Categorie":"VARSTA_12_15","proba":"CAUTAREA_UNEI_COMORI"}]);

    function handleAdd(concurs){
        console.log('Inside add Func '+concurs);
        AddConcurs(concurs)
            .then(res=> GetConcurs())
            .then(concourses=>setConcurses(concourses))
            .catch(erorr=>console.log('eroare add ',erorr));
    }
    function editFunction(concurs){
        console.log('Inside editFunc '+concurs);
        EditConcurs(concurs)
            .then(res=> GetConcurs())
            .then(concourses=>setConcurses(concourses))
            .catch(error=>console.log('eroare edit', error));
    }

    function deleteFunction(concurs){
        console.log('Inside deleteFunc '+concurs);
        DeleteConcurs(concurs)
            .then(res=> GetConcurs())
            .then(concourses=>setConcurses(concourses))
            .catch(error=>console.log('eroare delete', error));
    }



    useEffect(() => {
        console.log('Inside useEffect')
        GetConcurs().then(concourses => setConcurses(concourses));
    }, []);

    return (
        <div className="UserApp">
            <h1> Concurs Copii </h1>
            <div className="content">
                <div className="form-container">
                    <ConcursForm addConcurs={handleAdd} updateConcurs={editFunction} />
                </div>
                <div className="row-container">
                    <ConcursRow concurses={concurses} deleteFunction={deleteFunction} />
                </div>
            </div>
        </div>
    );
}