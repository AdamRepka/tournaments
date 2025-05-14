import { Dialog, DialogContent, DialogHeader, DialogFooter } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { useState } from "react"
import axios from "axios"

export default function MatchEditModal({ open, onClose, match, onSaved }) {
    const [sets, setSets] = useState(match?.matchSets || [{ player1Points: 0, player2Points: 0 }, { player1Points: 0, player2Points: 0 }])

    const handleSetChange = (index, player, value) => {
        const updated = [...sets]
        updated[index][player] = Number(value)
        setSets(updated)
    }

    const handleSave = async () => {
        const dto = {
            id: match?.id,
            player1Id: match.player1Id,
            player2Id: match.player2Id,
            tournamentGroupId: match.tournamentGroupId,
            matchSets: sets
        }
        const response = await axios.post("/api/groups/match/save", dto)
        onSaved(response.data)
        onClose()
    }

    return (
        <Dialog open={open} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <h2 className="text-xl font-bold">{match.player1Name} vs {match.player2Name}</h2>
                </DialogHeader>
                <div className="grid gap-4">
                    {sets.map((set, index) => (
                        <div key={index} className="flex items-center gap-2">
                            <span>Set {index + 1}:</span>
                            <input type="number" value={set.player1Points} onChange={e => handleSetChange(index, "player1Points", e.target.value)} className="border rounded px-2 w-16" />
                            <span>-</span>
                            <input type="number" value={set.player2Points} onChange={e => handleSetChange(index, "player2Points", e.target.value)} className="border rounded px-2 w-16" />
                        </div>
                    ))}
                </div>
                <DialogFooter>
                    <Button variant="outline" onClick={onClose}>Zrušit</Button>
                    <Button onClick={handleSave}>Uložit</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    )
}
