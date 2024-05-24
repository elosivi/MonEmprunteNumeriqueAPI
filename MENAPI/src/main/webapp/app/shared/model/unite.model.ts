export interface IUnite {
  id?: number;
  libelle?: string;
  estTemporelle?: boolean | null;
}

export const defaultValue: Readonly<IUnite> = {
  estTemporelle: false,
};
