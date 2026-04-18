# Algorithme de scoring IA
def calculer_score(budget: float,
                   localisation: str,
                   nb_interactions: int,
                   nb_likes: int,
                   nb_dislikes: int,
                   nb_visites: int,
                   statut: str) -> dict:

    score = 0.0

    # 1 — Score basé sur le budget (max 25 pts)
    if budget >= 1000000:
        score += 25
    elif budget >= 500000:
        score += 20
    elif budget >= 200000:
        score += 15
    elif budget >= 100000:
        score += 10
    else:
        score += 5

    # 2 — Score basé sur les interactions (max 35 pts)
    score += min(nb_likes * 10, 20)      # max 20 pts
    score -= min(nb_dislikes * 5, 10)    # max -10 pts
    score += min(nb_visites * 5, 15)     # max 15 pts

    # 3 — Score basé sur le statut (max 25 pts)
    statut_scores = {
        "NOUVEAU": 5,
        "CONTACTE": 10,
        "INTERESSE": 15,
        "EN_NEGOCIATION": 20,
        "CONVERTI": 25,
        "PERDU": 0
    }
    score += statut_scores.get(statut, 0)

    # 4 — Score basé sur nb total interactions (max 15 pts)
    score += min(nb_interactions * 2, 15)

    # Limiter entre 0 et 100
    score = max(0.0, min(100.0, score))

    # Catégorie
    if score >= 70:
        categorie = "CHAUD"
    elif score >= 40:
        categorie = "TIEDE"
    else:
        categorie = "FROID"

    return {
        "score": round(score, 2),
        "categorie": categorie
    }