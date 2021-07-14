package com.assessment.airport.airports.config;

import javax.persistence.EntityManagerFactory;

import com.assessment.airport.airports.domaine.Airport;
import com.assessment.airport.airports.repository.AirportRepository;

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
public class BatchConfigAirports {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    EntityManagerFactory emf;

    @Value("classPath:/input/airports.csv")
    private Resource inputResource;

    @Bean
    public Job readAirportCSVFileJob() {
        return jobBuilderFactory.get("readAirportCSVFileJob").incrementer(new RunIdIncrementer())
                .listener(listenerAirport()).start(stepAirport()).build();
    }

    @Bean
    public Step stepAirport() {
        return stepBuilderFactory.get("stepAirport").<Airport, Airport>chunk(500).reader(readerAirport())
                .processor(processorAirport()).writer(writerAirport()).build();
    }

    @Bean
    public ItemProcessor<Airport, Airport> processorAirport() {
        return new DBAirportLogProcessor();
    }

    @Bean
    public FlatFileItemReader<Airport> readerAirport() {
        FlatFileItemReader<Airport> itemReader = new FlatFileItemReader<Airport>();
        itemReader.setLineMapper(lineMapperAirport());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(inputResource);
        return itemReader;
    }

    @Bean
    public LineMapper<Airport> lineMapperAirport() {
        DefaultLineMapper<Airport> lineMapper = new DefaultLineMapper<Airport>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "id", "ident", "type", "name", "latitude_deg", "longitude_deg",
                "elevation_ft", "continent", "iso_country", "iso_region", "municipality", "scheduled_service",
                "gps_code", "iata_code", "local_code", "home_link", "wikipedia_link", "keywords" });
        // lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3, 4, 5 });

        BeanWrapperFieldSetMapper<Airport> fieldSetMapper = new BeanWrapperFieldSetMapper<Airport>();
        fieldSetMapper.setTargetType(Airport.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public JpaItemWriter<Airport> writerAirport() {

        JpaItemWriter<Airport> writer = new JpaItemWriter<Airport>();
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
    public JobExecutionListener listenerAirport() {
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
                     * airportRepository.findAll() .forEach(airport -> System.out.println(("Found <"
                     * + airport + "> in the database.")));
                     */
                }
            }
        };
    }

}
