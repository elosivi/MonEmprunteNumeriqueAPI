import { IMaterielProfil } from 'app/shared/model/materiel-profil.model';

export interface IMateriel {
  id?: number;
  libelle?: string;
  feVeille?: number | null;
  materielProfils?: IMaterielProfil[] | null;
}

export const defaultValue: Readonly<IMateriel> = {};
