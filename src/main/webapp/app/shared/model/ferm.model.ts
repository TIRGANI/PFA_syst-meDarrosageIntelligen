import { IParcelle } from '@/shared/model/parcelle.model';

export interface IFerm {
  id?: number;
  numParcelle?: number | null;
  photo?: string | null;
  parcelles?: IParcelle[] | null;
}

export class Ferm implements IFerm {
  constructor(
    public id?: number,
    public numParcelle?: number | null,
    public photo?: string | null,
    public parcelles?: IParcelle[] | null
  ) {}
}
