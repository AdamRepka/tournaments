import React, { useState } from 'react';

const AddPlayerForm = () => {
    const [player, setPlayer] = useState({
        firstName: '',
        lastName: '',
        category: 'SENIOR', // Toto odpovídá enum PlayerCategory
        skillLevel: 'ADVANCED', // Toto odpovídá enum SkillLevel
        code: '',
        entryFeePaid: false,
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (type === 'checkbox') {
            setPlayer((prev) => ({
                ...prev,
                [name]: checked, // pro checkbox nastavíme hodnotu na checked (true/false)
            }));
        } else {
            setPlayer((prev) => ({
                ...prev,
                [name]: value,
            }));
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        console.log('Odesílaná data:', player); // Pro kontrolu před odesláním

        // Posílání požadavku na backend
        fetch('http://localhost:8080/api/players', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                firstName: player.firstName,
                lastName: player.lastName,
                category: player.category, // Odesíláme kategorii (SENIOR, JUNIOR, WOMEN)
                skillLevel: player.skillLevel, // Odesíláme úroveň dovednosti (BASIC, ADVANCED)
                code: player.code,
                entryFeePaid: player.entryFeePaid,
                // Zbytek, jako statistiky, backend vygeneruje sám
            }),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log('Hráč přidán:', data);
                // Po úspěšném přidání hráče, resetujeme formulář
                setPlayer({
                    firstName: '',
                    lastName: '',
                    category: 'SENIOR',
                    skillLevel: 'ADVANCED',
                    code: '',
                    entryFeePaid: false,
                });
            })
            .catch((error) => console.error('Chyba při přidávání hráče:', error));
    };

    return (
        <div className="max-w-2xl mx-auto p-8 bg-white shadow-lg rounded-lg mt-8">
            <h2 className="text-2xl font-semibold text-gray-800 mb-6">Přidat hráče</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">Jméno:</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={player.firstName}
                        onChange={handleChange}
                        className="mt-1 p-2 w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Zadejte jméno"
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">Příjmení:</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={player.lastName}
                        onChange={handleChange}
                        className="mt-1 p-2 w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Zadejte příjmení"
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="category" className="block text-sm font-medium text-gray-700">Kategorie:</label>
                    <select
                        id="category"
                        name="category"
                        value={player.category}
                        onChange={handleChange}
                        className="mt-1 p-2 w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    >
                        <option value="SENIOR">SENIOR</option>
                        <option value="JUNIOR">JUNIOR</option>
                        <option value="WOMEN">WOMEN</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label htmlFor="skillLevel" className="block text-sm font-medium text-gray-700">Úroveň dovednosti:</label>
                    <select
                        id="skillLevel"
                        name="skillLevel"
                        value={player.skillLevel}
                        onChange={handleChange}
                        className="mt-1 p-2 w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    >
                        <option value="BASIC">ZÁKLADNÍ</option>
                        <option value="ADVANCED">POKROČILÝ</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label htmlFor="code" className="block text-sm font-medium text-gray-700">Kód hráče:</label>
                    <input
                        type="text"
                        id="code"
                        name="code"
                        value={player.code}
                        onChange={handleChange}
                        className="mt-1 p-2 w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Zadejte kód"
                    />
                </div>

                <div className="mb-4 flex items-center">
                    <input
                        type="checkbox"
                        id="entryFeePaid"
                        name="entryFeePaid"
                        checked={player.entryFeePaid}
                        onChange={handleChange}
                        className="mr-2"
                    />
                    <label htmlFor="entryFeePaid" className="text-sm text-gray-700">Startovné zaplaceno</label>
                </div>

                <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
                    Přidat hráče
                </button>
            </form>
        </div>
    );
};

export default AddPlayerForm;
