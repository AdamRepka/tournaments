import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useForm } from 'react-hook-form';
import { useParams, useNavigate } from 'react-router-dom';

const EditPlayer = () => {
    const { id } = useParams(); // předpoklad, že cesta má /players/edit/:id
    const navigate = useNavigate();
    const { register, handleSubmit, setValue } = useForm();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get(`/api/players/${id}`)
            .then(response => {
                const player = response.data;
                setValue('firstName', player.firstName);
                setValue('lastName', player.lastName);
                setValue('category', player.category);
                setValue('skillLevel', player.skillLevel);
                setValue('code', player.code);
                setValue('entryFeePaid', player.entryFeePaid);
                setLoading(false);
            })
            .catch(error => {
                console.error('Chyba při načítání hráče:', error);
                setLoading(false);
            });
    }, [id, setValue]);

    const onSubmit = data => {
        axios.put(`/api/players/${id}`, data)
            .then(() => {
                navigate('/player-list'); // přesměruje zpět na seznam hráčů
            })
            .catch(error => {
                console.error('Chyba při aktualizaci hráče:', error);
            });
    };

    if (loading) return <p>Načítám hráče...</p>;

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="p-4 max-w-md mx-auto bg-white rounded shadow">
            <div className="mb-2">
                <label className="block mb-1">Jméno:</label>
                <input {...register('firstName')} className="border w-full p-2" />
            </div>
            <div className="mb-2">
                <label className="block mb-1">Příjmení:</label>
                <input {...register('lastName')} className="border w-full p-2" />
            </div>
            <div className="mb-2">
                <label className="block mb-1">Kategorie:</label>
                <select {...register('category')} className="border w-full p-2">
                    <option value="SENIOR">Senior</option>
                    <option value="JUNIOR">Junior</option>
                    <option value="WOMEN">Ženy</option>
                </select>
            </div>
            <div className="mb-2">
                <label className="block mb-1">Úroveň dovednosti:</label>
                <select {...register('skillLevel')} className="border w-full p-2">
                    <option value="BASIC">Basic</option>
                    <option value="ADVANCED">Advanced</option>
                </select>
            </div>
            <div className="mb-2">
                <label className="block mb-1">Kód:</label>
                <input {...register('code')} className="border w-full p-2" />
            </div>
            <div className="mb-4">
                <label className="inline-flex items-center">
                    <input type="checkbox" {...register('entryFeePaid')} className="mr-2" />
                    Startovné zaplaceno
                </label>
            </div>
            <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
                Uložit změny
            </button>
        </form>
    );
};

export default EditPlayer;
