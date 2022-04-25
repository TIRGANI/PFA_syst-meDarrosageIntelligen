/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import GrandeurComponent from '@/entities/grandeur/grandeur.vue';
import GrandeurClass from '@/entities/grandeur/grandeur.component';
import GrandeurService from '@/entities/grandeur/grandeur.service';
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
  describe('Grandeur Management Component', () => {
    let wrapper: Wrapper<GrandeurClass>;
    let comp: GrandeurClass;
    let grandeurServiceStub: SinonStubbedInstance<GrandeurService>;

    beforeEach(() => {
      grandeurServiceStub = sinon.createStubInstance<GrandeurService>(GrandeurService);
      grandeurServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<GrandeurClass>(GrandeurComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          grandeurService: () => grandeurServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      grandeurServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllGrandeurs();
      await comp.$nextTick();

      // THEN
      expect(grandeurServiceStub.retrieve.called).toBeTruthy();
      expect(comp.grandeurs[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      grandeurServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(grandeurServiceStub.retrieve.callCount).toEqual(1);

      comp.removeGrandeur();
      await comp.$nextTick();

      // THEN
      expect(grandeurServiceStub.delete.called).toBeTruthy();
      expect(grandeurServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
