import { IAlerte } from '@/shared/model/alerte.model';
import { IBrache } from '@/shared/model/brache.model';
import { IAffectation } from '@/shared/model/affectation.model';

export interface IBoitier {
  id?: number;
  reference?: string | null;
  type?: string | null;
  alerte?: IAlerte | null;
  braches?: IBrache[] | null;
  affectations?: IAffectation[] | null;
}

export class Boitier implements IBoitier {
  constructor(
    public id?: number,
    public reference?: string | null,
    public type?: string | null,
    public alerte?: IAlerte | null,
    public braches?: IBrache[] | null,
    public affectations?: IAffectation[] | null
  ) {}
}
