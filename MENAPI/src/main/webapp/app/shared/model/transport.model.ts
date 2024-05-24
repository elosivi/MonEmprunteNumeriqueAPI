import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { TypeMoteur } from 'app/shared/model/enumerations/type-moteur.model';

export interface ITransport {
  id?: number;
  categorie?: string;
  typeMoteur?: keyof typeof TypeMoteur | null;
  feKm?: number | null;
  transportProfils?: ITransportProfil[] | null;
  deplacementProfils?: IDeplacementProfil[] | null;
}

export const defaultValue: Readonly<ITransport> = {};
