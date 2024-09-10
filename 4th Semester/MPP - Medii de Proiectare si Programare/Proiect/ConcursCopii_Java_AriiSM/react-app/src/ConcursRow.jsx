import React from  'react'
import './styles.css';
function ConcursRow({concurs, deleteFunction}) {
    function deleteConcurs() {
        console.log('Delete button for concurs ' + concurs.id + ' pressed');
        deleteFunction(concurs.id);
    }


    return (
        <tr>
            <td>{concurs.id}</td>
            <td>{concurs.categorie}</td>
            <td>{concurs.proba}</td>
            <td>
                <button onClick={deleteConcurs}> Delete</button>
            </td>
        </tr>
    );
}

export default function ConcursTable({concurses, deleteFunction, editFunction}) {
    console.log("In ConcursTable");
    console.log(concurses);
    let rows = [];
    concurses.forEach(function (concurs) {
        rows.push(<ConcursRow concurs={concurs} key={concurs.id} deleteFunction={deleteFunction} editFunction={editFunction}/>);
    });
    return (
        <div className="ConcursTable">

            <table className="center" style={{borderSpacing: '10px', borderCollapse: 'separate'}}>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Categorie</th>
                    <th>Proba</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

        </div>
    );
}