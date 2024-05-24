import { IProfil } from 'app/shared/model/profil.model';
import { IPrestation } from 'app/shared/model/prestation.model';

export interface IPrestationProfil {
  id?: number;
  nbMoisPresta?: number | null;
  nbSemCongesEstime?: number | null;
  dureeHebdo?: number | null;
  dureeTeletravail?: number | null;
  dureeReuAudio?: number | null;
  dureeReuVisio?: number | null;
  nbMailsSansPJ?: number | null;
  nbMailsAvecPJ?: number | null;
  veillePause?: boolean | null;
  veilleSoir?: boolean | null;
  veilleWeekend?: boolean | null;
  nbTerminaux?: number | null;
  nbDeplacements?: number | null;
  ecMensuelle?: number | null;
  ecTotalePreta?: number | null;
  ecTransportMensuel?: number | null;
  ecFabMateriel?: number | null;
  ecUtilMaterielMensuel?: number | null;
  ecCommMensuel?: number | null;
  profil?: IProfil | null;
  prestations?: IPrestation[] | null;
}

export const defaultValue: Readonly<IPrestationProfil> = {
  veillePause: false,
  veilleSoir: false,
  veilleWeekend: false,
};
