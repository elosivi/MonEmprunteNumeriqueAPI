import { IFonction } from 'app/shared/model/fonction.model';
import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';

export interface IProfil {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  email?: string;
  fonction?: IFonction | null;
  transportProfils?: ITransportProfil[] | null;
  deplacementProfils?: IDeplacementProfil[] | null;
  materielProfils?: IMaterielProfil[] | null;
}

export const defaultValue: Readonly<IProfil> = {};
