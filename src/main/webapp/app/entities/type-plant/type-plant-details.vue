<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="typePlant">
        <h2 class="jh-entity-heading" data-cy="typePlantDetailsHeading">
          <span v-text="$t('pfaApp.typePlant.detail.title')">TypePlant</span> {{ typePlant.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('pfaApp.typePlant.lebelle')">Lebelle</span>
          </dt>
          <dd>
            <span>{{ typePlant.lebelle }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.typePlant.humiditeMax')">Humidite Max</span>
          </dt>
          <dd>
            <span>{{ typePlant.humiditeMax }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.typePlant.humiditeMin')">Humidite Min</span>
          </dt>
          <dd>
            <span>{{ typePlant.humiditeMin }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.typePlant.temperature')">Temperature</span>
          </dt>
          <dd>
            <span>{{ typePlant.temperature }}</span>
          </dd>
          <dt>
            <span v-text="$t('pfaApp.typePlant.luminisite')">Luminisite</span>
          </dt>
          <dd>
            <span>{{ typePlant.luminisite }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="typePlant.id"
          :to="{ name: 'TypePlantEdit', params: { typePlantId: typePlant.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./type-plant-details.component.ts"></script>
