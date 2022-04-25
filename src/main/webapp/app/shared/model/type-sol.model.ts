import { IParcelle } from '@/shared/model/parcelle.model';

export interface ITypeSol {
  id?: number;
  libelle?: number | null;
  type?: string | null;
  parcelles?: IParcelle[] | null;
}

export class TypeSol implements ITypeSol {
  constructor(public id?: number, public libelle?: number | null, public type?: string | null, public parcelles?: IParcelle[] | null) {}
}
