<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="boitier">
        <h2 class="jh-entity-heading" data-cy="boitierDetailsHeading">
          <span v-text="$t('pfaApp.boitier.detail.title')">Boitier</span> {{ boitier.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.boitier.reference')">Reference</span>
          </dt>
          <dd>
            <span>{{ boitier.reference }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.boitier.type')">Type</span>
          </dt>
          <dd>
            <span>{{ boitier.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.boitier.alerte')">Alerte</span>
          </dt>
          <dd>
            <div v-if="boitier.alerte">
              <router-link :to="{ name: 'AlerteView', params: { alerteId: boitier.alerte.id } }">{{ boitier.alerte.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.boitier.brache')">Brache</span>
          </dt>
          <dd>
            <span v-for="(brache, i) in boitier.braches" :key="brache.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'BracheView', params: { bracheId: brache.id } }">{{ brache.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="boitier.id" :to="{ name: 'BoitierEdit', params: { boitierId: boitier.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./boitier-details.component.ts"></script>
