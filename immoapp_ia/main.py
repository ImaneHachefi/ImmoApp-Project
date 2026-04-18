from fastapi import FastAPI
from pydantic import BaseModel
from model.scoring import calculer_score

app = FastAPI(title="ImmoApp IA Service", version="1.0")

# ── Modèles Pydantic ──

class ScoreRequest(BaseModel):
    budget: float = 0.0
    localisation: str = ""
    nb_interactions: int = 0
    nb_likes: int = 0
    nb_dislikes: int = 0
    nb_visites: int = 0
    statut: str = "NOUVEAU"

class ScoreResponse(BaseModel):
    score: float
    categorie: str

# ── Endpoints ──

@app.get("/")
def root():
    return {"message": "ImmoApp IA Service is running !"}

@app.post("/score", response_model=ScoreResponse)
def calculer_score_prospect(request: ScoreRequest):
    result = calculer_score(
        budget=request.budget,
        localisation=request.localisation,
        nb_interactions=request.nb_interactions,
        nb_likes=request.nb_likes,
        nb_dislikes=request.nb_dislikes,
        nb_visites=request.nb_visites,
        statut=request.statut
    )
    return ScoreResponse(
        score=result["score"],
        categorie=result["categorie"]
    )

@app.get("/health")
def health():
    return {"status": "UP"}