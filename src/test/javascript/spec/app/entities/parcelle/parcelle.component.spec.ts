/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ParcelleComponent from '@/entities/parcelle/parcelle.vue';
import ParcelleClass from '@/entities/parcelle/parcelle.component';
import ParcelleService from '@/entities/parcelle/parcelle.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Parcelle Management Component', () => {
    let wrapper: Wrapper<ParcelleClass>;
    let comp: ParcelleClass;
    let parcelleServiceStub: SinonStubbedInstance<ParcelleService>;

    beforeEach(() => {
      parcelleServiceStub = sinon.createStubInstance<ParcelleService>(ParcelleService);
      parcelleServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ParcelleClass>(ParcelleComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          parcelleService: () => parcelleServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      parcelleServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllParcelles();
      await comp.$nextTick();

      // THEN
      expect(parcelleServiceStub.retrieve.called).toBeTruthy();
      expect(comp.parcelles[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      parcelleServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(parcelleServiceStub.retrieve.callCount).toEqual(1);

      comp.removeParcelle();
      await comp.$nextTick();

      // THEN
      expect(parcelleServiceStub.delete.called).toBeTruthy();
      expect(parcelleServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
