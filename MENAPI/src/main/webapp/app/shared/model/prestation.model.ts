import dayjs from 'dayjs';
import { IPrestationProfil } from 'app/shared/model/prestation-profil.model';
import { ITransportProfil } from 'app/shared/model/transport-profil.model';
import { IDeplacementProfil } from 'app/shared/model/deplacement-profil.model';
import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';
import { TypePresta } from 'app/shared/model/enumerations/type-presta.model';
import { LieuPresta } from 'app/shared/model/enumerations/lieu-presta.model';

export interface IPrestation {
  id?: number;
  nomPrestation?: string;
  nomUtilisateur?: string;
  nomMission?: string;
  nomClient?: string;
  ecUnite?: string;
  ecMensuelle?: number;
  ecTotale?: number | null;
  ecTransportMensuel?: number | null;
  ecFabMateriel?: number | null;
  ecUtilMaterielMensuel?: number | null;
  ecCommMensuel?: number | null;
  nbrProfils?: number;
  dureeMois?: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  typePresta?: keyof typeof TypePresta;
  lieupresta?: keyof typeof LieuPresta;
  donneesSaisies?: boolean | null;
  donneesReperes?: boolean | null;
  prestationProfils?: IPrestationProfil[] | null;
  transportProfils?: ITransportProfil[] | null;
  deplacementProfils?: IDeplacementProfil[] | null;
  materielProfils?: IMaterielProfil[] | null;
}

export const defaultValue: Readonly<IPrestation> = {
  donneesSaisies: false,
  donneesReperes: false,
};
