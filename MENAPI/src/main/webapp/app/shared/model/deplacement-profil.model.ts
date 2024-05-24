import { IProfil } from 'app/shared/model/profil.model';
import { IPrestation } from 'app/shared/model/prestation.model';
import { ITransport } from 'app/shared/model/transport.model';

export interface IDeplacementProfil {
  id?: number;
  nbDeplacement?: number | null;
  kmPresta?: number | null;
  profils?: IProfil[] | null;
  prestations?: IPrestation[] | null;
  transports?: ITransport[] | null;
}

export const defaultValue: Readonly<IDeplacementProfil> = {};
