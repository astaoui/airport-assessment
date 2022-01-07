package com.assessment.airport.airports.config;

import javax.persistence.EntityManagerFactory;

import com.assessment.airport.airports.domaine.Runway;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfigRunways {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /*
     * @Autowired private RunwayRepository runwayRepository;
     */

    @Autowired
    EntityManagerFactory emf;

    @Value("classPath:/input/runways.csv")
    private Resource inputResource;

    @Bean
    public Job readRunwayCSVFileJob() {
        return jobBuilderFactory.get("readRunwayCSVFileJob").incrementer(new RunIdIncrementer())
                .listener(listenerRunway()).start(stepRunway()).build();
    }

    @Bean
    public Step stepRunway() {
        return stepBuilderFactory.get("stepRunway").<Runway, Runway>chunk(500).reader(readerRunway())
                .processor(processorRunway()).writer(writerRunway()).build();
    }

    @Bean
    public ItemProcessor<Runway, Runway> processorRunway() {
        return new DBRunwayLogProcessor();
    }

    @Bean
    public FlatFileItemReader<Runway> readerRunway() {
        FlatFileItemReader<Runway> itemReader = new FlatFileItemReader<Runway>();
        itemReader.setLineMapper(lineMapperRunway());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(inputResource);
        return itemReader;
    }

    @Bean
    public LineMapper<Runway> lineMapperRunway() {
        DefaultLineMapper<Runway> lineMapper = new DefaultLineMapper<Runway>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "id", "airport_ref", "airport_ident", "length_ft", "width_ft", "surface",
                "lighted", "closed", "le_ident", "le_latitude_deg", "le_longitude_deg", "le_elevation_ft",
                "le_heading_degT", "le_displaced_threshold_ft", "he_ident", "he_latitude_deg", "he_longitude_deg",
                "he_elevation_ft", "he_heading_degT", "he_displaced_threshold_ft" });

        // lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3, 4, 5 });

        BeanWrapperFieldSetMapper<Runway> fieldSetMapper = new BeanWrapperFieldSetMapper<Runway>();
        fieldSetMapper.setTargetType(Runway.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public JpaItemWriter<Runway> writerRunway() {

        JpaItemWriter<Runway> writer = new JpaItemWriter<Runway>();
        writer.setEntityManagerFactory(emf);
        return writer;
        /*
         *
         * itemWriter.setDataSource(dataSource()); itemWriter.setSql( "INSERT INTO
         * COUNTRY (id, code, name, continent, wikipedia );
         * itemWriter.setItemSqlParameterSourceProvider(new
         * BeanPropertyItemSqlParameterSourceProvider<Country>
         */
    }

    // @Bean
    /*
     * public DataSource dataSource() { EmbeddedDatabaseBuilder
     * embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder(); return
     * embeddedDatabaseBuilder.addScript(
     * "classpath:org/springframework/batch/core/schema-drop-h2.sql")
     * .addScript("classpath:org/springframework/batch/core/schema-h2.sql").setType(
     * EmbeddedDatabaseType.H2) .build(); }
     */

    @Bean
    public JobExecutionListener listenerRunway() {
        return new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {
                /**
                 * As of now empty but can add some before job conditions
                 */
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    System.out.println(("!!! JOB FINISHED! Time to verify the results"));
                    /*
                     * runwayRepository.findAll() .forEach(runway -> System.out.println(("Found <" +
                     * runway + "> in the database.")));
                     */
                }
            }
        };
    }

}
