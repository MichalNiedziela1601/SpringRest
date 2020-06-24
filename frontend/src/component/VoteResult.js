import * as React from "react";
import { withRouter} from "react-router";
import {Bar, BarChart, XAxis, YAxis} from "recharts";

class VoteResult extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isLoaded: false
        }
    }

    getVotesResult = (pollId) => {
        fetch(`${process.env.REACT_APP_API_HOST}/computeresult?pollId=${pollId}`)
            .then(res => res.json())
            .then(data => {
                this.setState({
                    result: data.results
                })
            })
            .catch(console.log);
    };

    getVotes = (pollId) => {
        fetch(`${process.env.REACT_APP_API_HOST}/polls/${pollId}`)
            .then(res => res.json())
            .then(data => {
                this.setState({
                    isLoaded: true,
                    options: data.options
                })
            })
            .catch(console.log)
    };

    componentDidMount() {
        let {pollId} = this.props.match.params;
        this.getVotesResult(pollId);
        this.getVotes(pollId);
    }

    addLabels = (result) => {
        let options = this.state.options;
        options.forEach(option => {
            if(result.optionId === option.id) {
                result["label"] = option.value;
            }
        });
        return result;
    };

    render() {
        const { result, isLoaded, options} = this.state;
        if (!result || !isLoaded || !options) return null;
        const data = result.map(result => this.addLabels(result));

        return(
            <div>
                <BarChart width={500} height={300} data={data}>
                    <YAxis dataKey="count" />
                    <XAxis dataKey="label"/>
                    <Bar dataKey="count" />
                </BarChart>
            </div>
        )
    }
}

export default withRouter(VoteResult);