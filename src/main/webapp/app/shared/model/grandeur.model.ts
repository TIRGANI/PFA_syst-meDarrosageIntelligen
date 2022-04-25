import { IParcelle } from '@/shared/model/parcelle.model';

export interface IGrandeur {
  id?: number;
  type?: string;
  valeur?: string;
  date?: string | null;
  parcelle?: IParcelle | null;
}

export class Grandeur implements IGrandeur {
  constructor(
    public id?: number,
    public type?: string,
    public valeur?: string,
    public date?: string | null,
    public parcelle?: IParcelle | null
  ) {}
}
