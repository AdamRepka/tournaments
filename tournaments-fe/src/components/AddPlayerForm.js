import React, { useState } from "react";

const AddPlayerForm = ({ onAddPlayer }) => {
    const [name, setName] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!name.trim()) return;

        onAddPlayer(name.trim());
        setName("");
    };

    return (
        <form onSubmit={handleSubmit} className="bg-white p-4 rounded shadow w-full max-w-md mx-auto">
            <h2 className="text-xl font-semibold mb-4 text-center">Přidat hráče</h2>
            <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Jméno hráče"
                className="border border-gray-300 rounded px-3 py-2 w-full mb-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
                type="submit"
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition w-full"
            >
                Přidat
            </button>
        </form>
    );
};

export default AddPlayerForm;
