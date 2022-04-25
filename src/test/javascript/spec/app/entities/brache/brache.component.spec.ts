/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import BracheComponent from '@/entities/brache/brache.vue';
import BracheClass from '@/entities/brache/brache.component';
import BracheService from '@/entities/brache/brache.service';
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
  describe('Brache Management Component', () => {
    let wrapper: Wrapper<BracheClass>;
    let comp: BracheClass;
    let bracheServiceStub: SinonStubbedInstance<BracheService>;

    beforeEach(() => {
      bracheServiceStub = sinon.createStubInstance<BracheService>(BracheService);
      bracheServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<BracheClass>(BracheComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          bracheService: () => bracheServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      bracheServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllBraches();
      await comp.$nextTick();

      // THEN
      expect(bracheServiceStub.retrieve.called).toBeTruthy();
      expect(comp.braches[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      bracheServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(bracheServiceStub.retrieve.callCount).toEqual(1);

      comp.removeBrache();
      await comp.$nextTick();

      // THEN
      expect(bracheServiceStub.delete.called).toBeTruthy();
      expect(bracheServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
