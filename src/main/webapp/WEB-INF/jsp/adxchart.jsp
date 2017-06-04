<%@ include file="include/header.jsp" %>

<div id="chart1">
	<button>Update</button>
<script src="http://d3js.org/d3.v4.min.js"></script>
<script src="http://techanjs.org/techan.min.js"></script>
<script>

    var margin = {top: 20, right: 20, bottom: 30, left: 50},
            width = 960 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom;

    var parseDate = d3.timeParse("%d-%b-%y");

    var x = techan.scale.financetime()
            .range([0, width]);

    var y = d3.scaleLinear()
            .range([height, 0]);

    var adx = techan.plot.adx()
            .xScale(x)
            .yScale(y);

    var xAxis = d3.axisBottom(x);

    var yAxis = d3.axisLeft(y)
            .tickFormat(d3.format(",.3s"));

    var svg = d3.select("#chart1").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    d3.json("/datajson?symbol=${chart2.symbol}", function(error, data) {
        var accessor = adx.accessor();

        data = data.map(function(d) {
            // Open, high, low, close generally not required, is being used here to demonstrate colored volume
            // bars
            return {
                date: parseDate(d.date),
                volume: +d.volume,
                open: +d.open,
                high: +d.high,
                low: +d.low,
                close: +d.close
            };
        }).sort(function(a, b) { return d3.ascending(accessor.d(a), accessor.d(b)); });

        svg.append("g")
                .attr("class", "adx");

        svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")");

        svg.append("g")
                .attr("class", "y axis")
                .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Average Directional Index");

        // Data to display initially
        draw(data.slice(0, data.length-20));
        // Only want this button to be active if the data has loaded
        d3.select("button").on("click", function() { draw(data); }).style("display", "inline");
    });

    function draw(data) {
        var adxData = techan.indicator.adx()(data);
        x.domain(adxData.map(adx.accessor().d));
        y.domain(techan.scale.plot.adx(adxData).domain());

        svg.selectAll("g.adx").datum(adxData).call(adx);
        svg.selectAll("g.x.axis").call(xAxis);
        svg.selectAll("g.y.axis").call(yAxis);
    }

</script>
</div>

<%@ include file="include/footer.jsp" %>