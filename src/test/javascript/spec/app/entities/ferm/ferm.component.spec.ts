/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FermComponent from '@/entities/ferm/ferm.vue';
import FermClass from '@/entities/ferm/ferm.component';
import FermService from '@/entities/ferm/ferm.service';
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
  describe('Ferm Management Component', () => {
    let wrapper: Wrapper<FermClass>;
    let comp: FermClass;
    let fermServiceStub: SinonStubbedInstance<FermService>;

    beforeEach(() => {
      fermServiceStub = sinon.createStubInstance<FermService>(FermService);
      fermServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FermClass>(FermComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          fermService: () => fermServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      fermServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFerms();
      await comp.$nextTick();

      // THEN
      expect(fermServiceStub.retrieve.called).toBeTruthy();
      expect(comp.ferms[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      fermServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(fermServiceStub.retrieve.callCount).toEqual(1);

      comp.removeFerm();
      await comp.$nextTick();

      // THEN
      expect(fermServiceStub.delete.called).toBeTruthy();
      expect(fermServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
