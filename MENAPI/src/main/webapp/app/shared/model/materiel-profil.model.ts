import { IProfil } from 'app/shared/model/profil.model';
import { IPrestation } from 'app/shared/model/prestation.model';
import { IMateriel } from 'app/shared/model/materiel.model';

export interface IMaterielProfil {
  id?: number;
  dureeHebdo?: number | null;
  estNeuf?: boolean | null;
  profils?: IProfil[] | null;
  prestations?: IPrestation[] | null;
  materiels?: IMateriel[] | null;
}

export const defaultValue: Readonly<IMaterielProfil> = {
  estNeuf: false,
};
