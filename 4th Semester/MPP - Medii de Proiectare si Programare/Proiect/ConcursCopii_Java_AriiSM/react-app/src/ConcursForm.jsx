import React from 'react';
import './styles.css';
import { useState } from 'react'

export default function ConcursForm({addConcurs, updateConcurs}) {
    const [id, setId] = useState("0");
    const [categorie, setCategorie] = useState("VARSTA_6_8");
    const [proba, setProba] = useState("DESEN");

    const categorii = ["VARSTA_6_8", "VARSTA_9_11", "VARSTA_12_15", "VARSTA_REST", "VARSTA_REST_MODIFICAT", "VARSTA_16_19","VARSTA_20_23","VARSTA_24_27","VARSTA_28_32","VARSTA_33_36"];
    const probe = ["DESEN", "CAUTAREA_UNEI_COMORI", "POEZIE", "PROBA_REST", "MATEMATICA", "SPORT","CICLISM","ALERGAT"];

    function handleAdd(event) {
        event.preventDefault();
        let concurs = { id, categorie, proba };
        addConcurs(concurs);
    }

    function handleUpdate(event) {
        event.preventDefault();
        let concurs = { id, categorie, proba };
        updateConcurs(concurs);
    }

    return (
        <form onSubmit={handleAdd}>
            <label>
                Id:
                <input type="text" value={id} onChange={e => setId(e.target.value)} />
            </label><br/>
            <label>
                Categorie:
                <select value={categorie} onChange={e => setCategorie(e.target.value)}>
                    {categorii.map((cat, index) => (
                        <option key={index} value={cat}>{cat}</option>
                    ))}
                </select>
            </label><br/>
            <label>
                Proba:
                <select value={proba} onChange={e => setProba(e.target.value)}>
                    {probe.map((prb, index) => (
                        <option key={index} value={prb}>{prb}</option>
                    ))}
                </select>
            </label><br/>
            <input type="submit" value="Add Concurs"/>
            <button onClick={handleUpdate}>Update Concurs</button>
        </form>
    )
}