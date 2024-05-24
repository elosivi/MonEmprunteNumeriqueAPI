import { IUnite } from 'app/shared/model/unite.model';

export interface IDonneesReferences {
  id?: number;
  libelle?: string;
  donneeReference?: number | null;
  unite?: IUnite | null;
  temporalite?: IUnite | null;
}

export const defaultValue: Readonly<IDonneesReferences> = {};
