// Modèles de domaine partagés pour les joueurs de tennis
// Ces interfaces décrivent la structure des données utilisées dans l'application.
export interface Country {
  picture: string; // URL de l'image du drapeau (ex. : "https://tenisu.latelier.co/resources/Espagne.png")
  code: string;    // Code du pays (ex. : "ESP")
}

export interface PlayerData {
  rank: number;
  points: number;
  weight: number; // en grammes
  height: number; // en centimètres
  age: number;
  last: number[]; // Résultats des derniers matchs (1 = victoire, 0 = défaite)
}

export interface Player {
  id: number;
  firstname: string;
  lastname: string;
  shortname: string;
  sex: string;
  country: Country;
  picture: string; // URL de l'image du joueur
  data: PlayerData;
}
